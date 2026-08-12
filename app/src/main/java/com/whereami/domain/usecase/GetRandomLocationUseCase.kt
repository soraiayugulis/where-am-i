package com.whereami.domain.usecase

import com.whereami.domain.model.Location
import com.whereami.domain.repository.RandomGenerator
import com.whereami.domain.repository.SeedRepository
import javax.inject.Inject

class GetRandomLocationUseCase @Inject constructor(
    private val seedRepository: SeedRepository,
    private val randomGenerator: RandomGenerator
) {
    operator fun invoke(locationSeed: Int): Result<Location> = runCatching {
        val seeds = seedRepository.load()
        require(seeds.isNotEmpty()) { "No seed locations available" }

        val index = randomGenerator.generate(locationSeed, seeds.size)
        val seed = seeds[index]

        Location(seed.lat, seed.lng)
    }
}
