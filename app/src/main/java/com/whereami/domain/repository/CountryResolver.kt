package com.whereami.domain.repository

import com.whereami.domain.model.Country
import com.whereami.domain.model.Location

interface CountryResolver {
    suspend fun resolve(location: Location): Country?
}
