package com.whereami.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.model.Location
import com.whereami.domain.session.GameSessionManager
import com.whereami.domain.session.GameSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessMapViewModel @Inject constructor(
    private val gameSessionManager: GameSessionManager
) : ViewModel() {
    val state: StateFlow<GameSessionState> = gameSessionManager.state

    private val _guess = MutableStateFlow<Location?>(null)
    val guess: StateFlow<Location?> = _guess

    fun selectGuess(location: Location) {
        _guess.value = location
    }

    fun confirmGuess() {
        val selected = _guess.value ?: return
        viewModelScope.launch {
            gameSessionManager.submitGuess(selected)
        }
    }
}
