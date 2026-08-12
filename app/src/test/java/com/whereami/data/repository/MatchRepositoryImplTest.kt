package com.whereami.data.repository

import com.whereami.data.local.MatchDao
import com.whereami.data.local.MatchEntity
import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MatchRepositoryImplTest {
    @Test
    fun `save inserts mapped entity`() = runBlocking {
        val dao = FakeMatchDao()
        val repo = MatchRepositoryImpl(dao)
        val match = MatchResult(
            datePlayed = 1000L,
            target = Location(10.123456, 20.654321),
            guess = null,
            timeTakenMs = 150_000L,
            score = 0,
            status = Status.INCOMPLETE
        )
        repo.save(match)

        assertEquals(1, dao.inserted.size)
        assertEquals("10.12346,20.65432", dao.inserted[0].targetLatLng)
        assertEquals(null, dao.inserted[0].guessLatLng)
        assertEquals("INCOMPLETE", dao.inserted[0].status)
    }

    @Test
    fun `getAll returns mapped domain models`() = runBlocking {
        val dao = FakeMatchDao().apply {
            stored.add(
                MatchEntity(
                    id = 1,
                    datePlayed = 1000L,
                    targetLatLng = "0.00000,0.00000",
                    guessLatLng = "1.00000,1.00000",
                    timeTakenMs = 30000L,
                    score = 4000,
                    status = "COMPLETED"
                )
            )
        }
        val repo = MatchRepositoryImpl(dao)
        val all = repo.getAll()

        assertEquals(1, all.size)
        assertEquals(1L, all[0].id)
        assertEquals(Location(0.0, 0.0), all[0].target)
        assertEquals(Location(1.0, 1.0), all[0].guess)
        assertEquals(Status.COMPLETED, all[0].status)
    }

    private class FakeMatchDao : MatchDao {
        val inserted = mutableListOf<MatchEntity>()
        val stored = mutableListOf<MatchEntity>()

        override suspend fun insert(match: MatchEntity): Long {
            inserted.add(match)
            return 1L
        }

        override suspend fun getAll(): List<MatchEntity> = stored

        override suspend fun getById(id: Long): MatchEntity? = null
    }
}
