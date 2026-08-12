package com.whereami.core.location

import com.whereami.domain.model.Location
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DistanceCalculatorTest {
    @Test
    fun `same location has distance 0`() {
        val d = DistanceCalculator().distanceInKm(Location(0.0, 0.0), Location(0.0, 0.0))
        assertEquals(0.0, d, 0.0001)
    }

    @Test
    fun `opposite points are near half the earth circumference`() {
        val d = DistanceCalculator().distanceInKm(Location(0.0, 0.0), Location(0.0, 180.0))
        assertTrue(d in 20_000.0..20_100.0)
    }
}
