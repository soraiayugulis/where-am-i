package com.whereami.domain.usecase

import com.whereami.core.location.DistanceCalculator
import com.whereami.domain.model.Country
import com.whereami.domain.model.Location
import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor(
    private val distanceCalculator: DistanceCalculator,
    private val distanceScore: CalculateDistanceScoreUseCase,
    private val countryBonus: CalculateCountryBonusUseCase
) {
    operator fun invoke(
        target: Location,
        guess: Location,
        targetCountry: Country? = null,
        guessCountry: Country? = null
    ): Int {
        val distanceKm = distanceCalculator.distanceInKm(target, guess)
        val base = distanceScore(distanceKm)
        val bonus = countryBonus(targetCountry, guessCountry)
        return (base + bonus).coerceAtMost(5000)
    }
}
