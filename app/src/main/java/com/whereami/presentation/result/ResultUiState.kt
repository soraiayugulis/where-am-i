package com.whereami.presentation.result

import com.whereami.domain.model.Location
import com.whereami.domain.model.PlaceAddress
import com.whereami.domain.model.Status

data class ResultUiState(
    val status: Status,
    val target: Location,
    val guess: Location?,
    val targetAddress: PlaceAddress?,
    val guessAddress: PlaceAddress?,
    val distanceKm: Double?,
    val score: Int
)
