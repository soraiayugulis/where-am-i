package com.whereami.data.repository

import com.whereami.data.seed.SeedDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class SeedRepositoryImplTest {
    @Test
    fun `seed repository returns parsed locations`() {
        val repo = SeedRepositoryImpl(FakeSeedDataSource(jsonWithOneLocation))
        val seeds = repo.load()

        assertEquals(1, seeds.size)
        assertEquals("Test", seeds[0].city)
        assertEquals("TT", seeds[0].countryCode)
    }

    @Test
    fun `loader throws on empty seed file`() {
        val repo = SeedRepositoryImpl(FakeSeedDataSource("[]"))
        try {
            repo.load()
            fail("expected exception")
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test
    fun `loader throws on blank seed file`() {
        val repo = SeedRepositoryImpl(FakeSeedDataSource("   "))
        try {
            repo.load()
            fail("expected exception")
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test
    fun `loader throws on invalid coordinates`() {
        val repo = SeedRepositoryImpl(FakeSeedDataSource(jsonWithInvalidLocation))
        try {
            repo.load()
            fail("expected exception")
        } catch (e: IllegalArgumentException) {
            // expected
        }
    }

    private companion object {
        const val jsonWithOneLocation = """[{"lat":12.34564,"lng":-87.65432,"city":"Test","countryCode":"TT"}]"""
        const val jsonWithInvalidLocation = """[{"lat":99.0,"lng":0.0,"city":"Invalid","countryCode":"IV"}]"""
    }

    private class FakeSeedDataSource(private val json: String) : SeedDataSource {
        override fun load(): String = json
    }
}
