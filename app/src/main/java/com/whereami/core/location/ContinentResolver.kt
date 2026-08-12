package com.whereami.core.location

import com.whereami.domain.model.Continent
import com.whereami.domain.model.Location

interface ContinentResolver {
    suspend fun resolve(location: Location): Continent?
}
