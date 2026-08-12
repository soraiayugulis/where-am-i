package com.whereami.domain.usecase

import com.whereami.domain.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateCountryBonusUseCaseTest {
    private val useCase = CalculateCountryBonusUseCase()

    @Test
    fun `same country returns 1000`() {
        val country = Country("BR", "Brazil")
        assertEquals(1000, useCase(country, country))
    }

    @Test
    fun `different country returns 0`() {
        val brazil = Country("BR", "Brazil")
        val argentina = Country("AR", "Argentina")
        assertEquals(0, useCase(brazil, argentina))
    }

    @Test
    fun `missing country returns 0`() {
        val brazil = Country("BR", "Brazil")
        assertEquals(0, useCase(brazil, null))
        assertEquals(0, useCase(null, brazil))
        assertEquals(0, useCase(null, null))
    }
}
