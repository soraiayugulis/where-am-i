package com.whereami.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MatchDaoTest {
    private lateinit var db: MatchDatabase
    private lateinit var dao: MatchDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MatchDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.matchDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `dao inserts and returns matches ordered by date descending`() = runBlocking {
        val older = MatchEntity(
            datePlayed = 1000L,
            targetLatLng = "0.00000,0.00000",
            guessLatLng = null,
            timeTakenMs = 150_000L,
            score = 0,
            status = "INCOMPLETE"
        )
        val newer = MatchEntity(
            datePlayed = 2000L,
            targetLatLng = "1.00000,1.00000",
            guessLatLng = "1.10000,1.10000",
            timeTakenMs = 30_000L,
            score = 4000,
            status = "COMPLETED"
        )
        dao.insert(older)
        dao.insert(newer)

        val all = dao.getAll()
        assertEquals(2, all.size)
        assertEquals(newer.datePlayed, all[0].datePlayed)
    }
}
