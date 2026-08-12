package com.whereami.domain.model

data class MatchResult(
    val id: Long = 0,
    val datePlayed: Long,
    val target: Location,
    val guess: Location?,
    val timeTakenMs: Long,
    val score: Int,
    val status: Status
) {
    val targetLatLng: String
        get() = target.toLatLngString()

    val guessLatLng: String?
        get() = guess?.toLatLngString()
}
