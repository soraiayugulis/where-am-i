package com.whereami.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.model.MatchResult
import com.whereami.domain.usecase.GetMatchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: GetMatchHistoryUseCase
) : ViewModel() {
    private val _groups = MutableStateFlow<List<MatchDayGroup>>(emptyList())
    val groups: StateFlow<List<MatchDayGroup>> = _groups

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _groups.value = getHistory().groupByDay()
    }

    private fun List<MatchResult>.groupByDay(): List<MatchDayGroup> =
        groupBy { it.datePlayed.toDayStart() }
            .toSortedMap(reverseOrder())
            .map { (day, matches) ->
                MatchDayGroup(day, matches.sortedByDescending { it.datePlayed })
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
    val matches: List<MatchResult>
)
