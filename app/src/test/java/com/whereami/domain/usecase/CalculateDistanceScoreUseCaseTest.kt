package com.whereami.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateDistanceScoreUseCaseTest {
    private val useCase = CalculateDistanceScoreUseCase()

    @Test
    fun `0 km returns 4000`() {
        assertEquals(4000, useCase(0.0))
    }

    @Test
    fun `score decays exponentially with distance`() {
        val at2500 = useCase(2500.0)
        val at5000 = useCase(5000.0)

        assertTrue(at5000 < at2500)
        assertTrue(at2500 < 4000)
    }

    @Test
    fun `very large distance returns 0`() {
        assertEquals(0, useCase(50_000.0))
    }
}
