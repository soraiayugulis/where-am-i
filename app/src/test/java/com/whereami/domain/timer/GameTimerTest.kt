package com.whereami.domain.timer

import com.whereami.core.time.FakeClock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameTimerTest {
    @Test
    fun `timer counts down from 150 to 0`() = runTest {
        val clock = FakeClock()
        val gameTimer = GameTimer(clock)
        gameTimer.start(this)

        try {
            assertEquals(150, gameTimer.remainingTime.value)

            repeat(150) {
                clock.advance(1000)
                testScheduler.advanceTimeBy(1000)
            }

            assertEquals(0, gameTimer.remainingTime.value)
        } finally {
            gameTimer.stop()
        }
    }

    @Test
    fun `timer warning is active when 10 seconds or less`() = runTest {
        val clock = FakeClock()
        val gameTimer = GameTimer(clock)
        gameTimer.start(this)

        try {
            repeat(140) {
                clock.advance(1000)
                testScheduler.advanceTimeBy(1000)
            }

            assertEquals(10, gameTimer.remainingTime.value)
            assertTrue(gameTimer.isWarning.value)
        } finally {
            gameTimer.stop()
        }
    }
}
