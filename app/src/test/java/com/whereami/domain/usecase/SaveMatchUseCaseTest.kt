package com.whereami.domain.usecase

import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import com.whereami.domain.repository.MatchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveMatchUseCaseTest {
    @Test
    fun `save delegates to repository`() = runBlocking {
        val fake = FakeMatchRepository()
        val useCase = SaveMatchUseCase(fake)
        val match = MatchResult(
            datePlayed = 1000L,
            target = Location(0.0, 0.0),
            guess = null,
            timeTakenMs = 150_000L,
            score = 0,
            status = Status.INCOMPLETE
        )

        useCase(match)

        assertEquals(1, fake.saved.size)
        assertEquals(match, fake.saved[0])
    }

    private class FakeMatchRepository : MatchRepository {
        val saved = mutableListOf<MatchResult>()

        override suspend fun save(match: MatchResult) {
            saved.add(match)
        }

        override suspend fun getAll(): List<MatchResult> = emptyList()
    }
}
