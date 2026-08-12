package com.whereami.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.session.GameSessionManager
import com.whereami.domain.session.GameSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StreetViewViewModel @Inject constructor(
    private val gameSessionManager: GameSessionManager
) : ViewModel() {
    val state: StateFlow<GameSessionState> = gameSessionManager.state

    fun startGame(locationSeed: Int) {
        gameSessionManager.start(locationSeed, viewModelScope)
    }
}
