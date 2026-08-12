package com.whereami.domain.usecase

import com.whereami.core.location.DistanceCalculator
import com.whereami.domain.model.Country
import com.whereami.domain.model.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateScoreUseCaseTest {
    private val useCase = CalculateScoreUseCase(
        DistanceCalculator(),
        CalculateDistanceScoreUseCase(),
        CalculateCountryBonusUseCase()
    )

    @Test
    fun `perfect guess with same country gives 5000`() {
        val country = Country("BR", "Brazil")
        val location = Location(-23.55, -46.63)
        val score = useCase(location, location, country, country)
        assertEquals(5000, score)
    }

    @Test
    fun `perfect guess with different country gives 4000`() {
        val brazil = Country("BR", "Brazil")
        val argentina = Country("AR", "Argentina")
        val location = Location(-23.55, -46.63)
        val score = useCase(location, location, brazil, argentina)
        assertEquals(4000, score)
    }

    @Test
    fun `no country bonus gives only distance score`() {
        val target = Location(0.0, 0.0)
        val guess = Location(10.0, 10.0)
        val score = useCase(target, guess, null, null)
        assertEquals(true, score in 1..4000)
    }
}
