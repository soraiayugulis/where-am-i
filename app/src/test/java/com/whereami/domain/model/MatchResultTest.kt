package com.whereami.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MatchResultTest {
    @Test
    fun `match result requires non-null target and optional guess`() {
        val target = Location(12.345641, -87.654321)
        val result = MatchResult(
            datePlayed = 1_700_000_000L,
            target = target,
            guess = null,
            timeTakenMs = 150_000L,
            score = 0,
            status = Status.INCOMPLETE
        )

        assertNotNull(result.target)
        assertNull(result.guess)
        assertEquals("12.34564,-87.65432", result.targetLatLng)
        assertNull(result.guessLatLng)
    }
}
