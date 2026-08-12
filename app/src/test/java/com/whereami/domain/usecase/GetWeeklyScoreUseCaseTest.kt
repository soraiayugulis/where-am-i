package com.whereami.domain.usecase

import com.whereami.core.time.TimeProvider
import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import com.whereami.domain.repository.MatchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWeeklyScoreUseCaseTest {
    @Test
    fun `sums only matches within last 168 hours`() = runBlocking {
        val now = 1_000_000L
        val week = 168L * 60L * 60L * 1000L
        val fakeRepo = FakeMatchRepository(
            listOf(
                matchAt(now, 1000),
                matchAt(now - week + 1, 2000),
                matchAt(now - week - 1, 500)
            )
        )
        val useCase = GetWeeklyScoreUseCase(fakeRepo, FixedTimeProvider(now))

        val score = useCase()

        assertEquals(3000, score)
    }

    @Test
    fun `exactly 168 hours old is included`() = runBlocking {
        val now = 1_000_000L
        val week = 168L * 60L * 60L * 1000L
        val fakeRepo = FakeMatchRepository(listOf(matchAt(now - week, 1000)))
        val useCase = GetWeeklyScoreUseCase(fakeRepo, FixedTimeProvider(now))

        assertEquals(1000, useCase())
    }

    private fun matchAt(date: Long, score: Int) = MatchResult(
        datePlayed = date,
        target = Location(0.0, 0.0),
        guess = null,
        timeTakenMs = 150_000L,
        score = score,
        status = Status.COMPLETED
    )

    private class FixedTimeProvider(private val now: Long) : TimeProvider {
        override fun nowMs(): Long = now
    }

    private class FakeMatchRepository(private val matches: List<MatchResult>) : MatchRepository {
        override suspend fun save(match: MatchResult) = Unit
        override suspend fun getAll(): List<MatchResult> = matches
    }
}
