package com.whereami.domain.repository

import com.whereami.domain.model.LocationSeed

interface SeedRepository {
    fun load(): List<LocationSeed>
}
