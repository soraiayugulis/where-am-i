package com.whereami.domain.session

import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult

sealed interface GameSessionState {
    object Idle : GameSessionState
    data class Playing(
        val target: Location,
        val remainingSeconds: Int,
        val isWarning: Boolean
    ) : GameSessionState
    data class Finished(val matchResult: MatchResult) : GameSessionState
}
