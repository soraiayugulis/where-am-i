package com.whereami.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.session.GameSessionManager
import com.whereami.domain.session.GameSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random
import javax.inject.Inject

private const val MAX_COVERAGE_ATTEMPTS = 5

@HiltViewModel
class StreetViewViewModel @Inject constructor(
    private val gameSessionManager: GameSessionManager
) : ViewModel() {
    val state: StateFlow<GameSessionState> = gameSessionManager.state

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError

    private var coverageAttempts = 0

    init {
        gameSessionManager.start(Random.nextInt(), viewModelScope)
    }

    fun onNoCoverage() {
        coverageAttempts++
        if (coverageAttempts >= MAX_COVERAGE_ATTEMPTS) {
            gameSessionManager.stop()
            _hasError.value = true
            return
        }
        gameSessionManager.start(Random.nextInt(), viewModelScope)
    }
}
