package com.whereami.presentation.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.repository.AddressResolver
import com.whereami.domain.usecase.GetAllMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val getAllMatches: GetAllMatchesUseCase,
    private val addressResolver: AddressResolver
) : ViewModel() {
    private val _topMatches = MutableStateFlow<List<RankingMatch>>(emptyList())
    val topMatches: StateFlow<List<RankingMatch>> = _topMatches

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        val matches = getAllMatches()
        val enriched = matches.map { match ->
            async {
                val guess = match.guess
                val address = guess?.let { addressResolver.resolve(it) }
                RankingMatch(match, address?.country)
            }
        }.awaitAll()

        val top = enriched.sortedByDescending { it.match.score }.take(5)
        _topMatches.value = top
        _totalScore.value = top.sumOf { it.match.score }
    }
}

data class RankingMatch(
    val match: com.whereami.domain.model.MatchResult,
    val guessedLocation: String?
)
