package com.whereami.domain.model

data class GameSession(
    val target: Location,
    val startTimeMs: Long,
    val guess: Location? = null,
    val endTimeMs: Long? = null,
    val status: Status = Status.INCOMPLETE,
    val score: Int = 0
)
