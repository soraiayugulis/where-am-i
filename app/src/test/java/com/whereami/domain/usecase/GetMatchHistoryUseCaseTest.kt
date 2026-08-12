package com.whereami.domain.usecase

import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import com.whereami.domain.repository.MatchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMatchHistoryUseCaseTest {
    @Test
    fun `invoke returns matches from repository`() = runBlocking {
        val match = MatchResult(
            datePlayed = 1000L,
            target = Location(0.0, 0.0),
            guess = null,
            timeTakenMs = 150_000L,
            score = 0,
            status = Status.INCOMPLETE
        )
        val fake = FakeMatchRepository(listOf(match))
        val useCase = GetMatchHistoryUseCase(fake)

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals(match, result[0])
    }

    private class FakeMatchRepository(private val matches: List<MatchResult>) : MatchRepository {
        override suspend fun save(match: MatchResult) = Unit
        override suspend fun getAll(): List<MatchResult> = matches
    }
}
