package com.whereami.domain.usecase

import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.floor

private const val MAX_BASE_SCORE = 4000
private const val SCORE_DECAY_CONSTANT = 2500.0

class CalculateDistanceScoreUseCase @Inject constructor() {
    operator fun invoke(distanceKm: Double): Int {
        val raw = MAX_BASE_SCORE * exp(-distanceKm / SCORE_DECAY_CONSTANT)
        return maxOf(0, floor(raw).toInt())
    }
}
