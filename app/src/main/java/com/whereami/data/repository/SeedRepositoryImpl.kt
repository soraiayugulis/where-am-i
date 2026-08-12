package com.whereami.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.whereami.data.seed.SeedDataSource
import com.whereami.domain.model.LocationSeed
import com.whereami.domain.repository.SeedRepository
import javax.inject.Inject

class SeedRepositoryImpl @Inject constructor(
    private val seedDataSource: SeedDataSource
) : SeedRepository {
    override fun load(): List<LocationSeed> {
        val json = seedDataSource.load().trim()
        if (json.isEmpty()) throw IllegalStateException("Seed file is empty")

        val type = object : TypeToken<List<LocationSeed>>() {}.type
        val seeds = Gson().fromJson<List<LocationSeed>>(json, type)
            ?: throw IllegalStateException("Seed file could not be parsed")

        if (seeds.isEmpty()) throw IllegalStateException("Seed file contains no locations")

        require(seeds.all { it.lat in -90.0..90.0 && it.lng in -180.0..180.0 }) {
            "Seed file contains invalid coordinates"
        }

        return seeds
    }
}
