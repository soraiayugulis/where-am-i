package com.whereami.domain.usecase

import com.whereami.domain.model.Location
import com.whereami.domain.model.LocationSeed
import com.whereami.domain.repository.RandomGenerator
import com.whereami.domain.repository.SeedRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GetRandomLocationUseCaseTest {
    private val seeds = listOf(
        LocationSeed(0.0, 0.0, "A", "AA"),
        LocationSeed(1.0, 1.0, "B", "BB"),
        LocationSeed(2.0, 2.0, "C", "CC")
    )

    @Test
    fun `same seed always selects the same target location`() {
        val useCase = GetRandomLocationUseCase(
            FakeSeedRepository(seeds),
            FakeRandomGenerator { seed, _ -> seed % seeds.size }
        )

        val first = useCase(1).getOrThrow()
        val second = useCase(1).getOrThrow()

        assertEquals(first, second)
    }

    @Test
    fun `different seeds select different target locations`() {
        val useCase = GetRandomLocationUseCase(
            FakeSeedRepository(seeds),
            FakeRandomGenerator { seed, _ -> seed % seeds.size }
        )

        val first = useCase(1).getOrThrow()
        val second = useCase(2).getOrThrow()

        assertNotEquals(first, second)
    }

    private class FakeSeedRepository(private val seeds: List<LocationSeed>) : SeedRepository {
        override fun load(): List<LocationSeed> = seeds
    }

    private class FakeRandomGenerator(private val block: (Int, Int) -> Int) : RandomGenerator {
        override fun generate(seed: Int, bound: Int): Int = block(seed, bound)
    }
}
