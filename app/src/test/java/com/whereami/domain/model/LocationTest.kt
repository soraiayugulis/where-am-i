package com.whereami.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationTest {
    @Test
    fun `location holds latitude and longitude as rounded 5-decimal strings`() {
        val location = Location(12.345641, -87.654321)
        assertEquals("12.34564,-87.65432", location.toLatLngString())
    }
}
