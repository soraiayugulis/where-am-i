package com.whereami.domain.session

import com.whereami.core.time.FakeClock
import com.whereami.domain.model.Location
import com.whereami.domain.model.LocationSeed
import com.whereami.domain.model.Status
import com.whereami.domain.repository.RandomGenerator
import com.whereami.domain.repository.SeedRepository
import com.whereami.domain.timer.GameTimer
import com.whereami.domain.usecase.GetRandomLocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameSessionManagerTest {
    @Test
    fun `expired match without guess is saved as INCOMPLETE with score 0`() = runTest {
        val clock = FakeClock()
        val (manager, _) = createManager(clock)
        manager.start(1, this)

        try {
            assertTrue(manager.state.value is GameSessionState.Playing)
            clock.advance(150_000)
            testScheduler.advanceTimeBy(150_000)
            val finished = manager.state.value as GameSessionState.Finished
            assertEquals(Status.INCOMPLETE, finished.matchResult.status)
            assertEquals(0, finished.matchResult.score)
            assertNull(finished.matchResult.guess)
            assertEquals(150_000, finished.matchResult.timeTakenMs)
        } finally {
            manager.stop()
        }
    }

    @Test
    fun `guess before expiration is saved as COMPLETED`() = runTest {
        val clock = FakeClock()
        val (manager, _) = createManager(clock)
        manager.start(1, this)

        try {
            val guess = Location(2.0, 2.0)
            clock.advance(30_000)
            testScheduler.advanceTimeBy(30_000)
            manager.submitGuess(guess)
            val finished = manager.state.value as GameSessionState.Finished
            assertEquals(Status.COMPLETED, finished.matchResult.status)
            assertEquals(guess, finished.matchResult.guess)
            assertEquals(30_000, finished.matchResult.timeTakenMs)
        } finally {
            manager.stop()
        }
    }

    private fun createManager(clock: FakeClock): Pair<GameSessionManager, FakeClock> {
        val seed = LocationSeed(1.0, 1.0, "Test", "TT")
        val useCase = GetRandomLocationUseCase(
            FakeSeedRepository(listOf(seed)),
            FakeRandomGenerator { _, _ -> 0 }
        )
        val gameTimer = GameTimer(clock)
        val manager = GameSessionManager(clock, gameTimer, useCase)
        return manager to clock
    }

    private class FakeSeedRepository(private val seeds: List<LocationSeed>) : SeedRepository {
        override fun load(): List<LocationSeed> = seeds
    }

    private class FakeRandomGenerator(private val block: (Int, Int) -> Int) : RandomGenerator {
        override fun generate(seed: Int, bound: Int): Int = block(seed, bound)
    }
}
