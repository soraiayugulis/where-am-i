package com.whereami.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.repository.AddressResolver
import com.whereami.domain.usecase.GetMatchHistoryUseCase
import com.whereami.domain.usecase.GetWeeklyScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: GetMatchHistoryUseCase,
    private val getWeeklyScore: GetWeeklyScoreUseCase,
    private val addressResolver: AddressResolver
) : ViewModel() {
    private val _groups = MutableStateFlow<List<MatchDayGroup>>(emptyList())
    val groups: StateFlow<List<MatchDayGroup>> = _groups

    private val _weeklyScore = MutableStateFlow(0)
    val weeklyScore: StateFlow<Int> = _weeklyScore

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _weeklyScore.value = getWeeklyScore()
        val matches = getHistory()
        val enriched = matches.map { match ->
            async {
                val guess = match.guess
                val address = guess?.let { addressResolver.resolve(it) }
                HistoryMatch(match, address?.country)
            }
        }.awaitAll()

        _groups.value = enriched.groupByDay()
    }

    private fun List<HistoryMatch>.groupByDay(): List<MatchDayGroup> =
        groupBy { it.match.datePlayed.toDayStart() }
            .toSortedMap(reverseOrder())
            .map { (day, matches) ->
                MatchDayGroup(day, matches.sortedByDescending { it.match.datePlayed })
            }

    private fun Long.toDayStart(): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = this@toDayStart
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}

data class MatchDayGroup(
    val dayStartMs: Long,
    val matches: List<HistoryMatch>
)

data class HistoryMatch(
    val match: com.whereami.domain.model.MatchResult,
    val guessedLocation: String?
)
