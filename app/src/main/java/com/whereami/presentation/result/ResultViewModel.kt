package com.whereami.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.core.location.DistanceCalculator
import com.whereami.domain.repository.AddressResolver
import com.whereami.domain.session.GameSessionManager
import com.whereami.domain.session.GameSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val gameSessionManager: GameSessionManager,
    private val addressResolver: AddressResolver,
    private val distanceCalculator: DistanceCalculator
) : ViewModel() {
    private val _state = MutableStateFlow<ResultUiState?>(null)
    val state: StateFlow<ResultUiState?> = _state

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        val finished = gameSessionManager.state.value as? GameSessionState.Finished ?: return@launch
        val match = finished.matchResult
        val targetAddress = addressResolver.resolve(match.target)
        val guessAddress = match.guess?.let { addressResolver.resolve(it) }
        val distanceKm = match.guess?.let { distanceCalculator.distanceInKm(match.target, it) }

        _state.value = ResultUiState(
            status = match.status,
            target = match.target,
            guess = match.guess,
            targetAddress = targetAddress,
            guessAddress = guessAddress,
            distanceKm = distanceKm,
            score = match.score
        )
    }
}
