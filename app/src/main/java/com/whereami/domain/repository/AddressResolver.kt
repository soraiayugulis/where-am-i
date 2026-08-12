package com.whereami.domain.repository

import com.whereami.domain.model.Location
import com.whereami.domain.model.PlaceAddress

interface AddressResolver {
    suspend fun resolve(location: Location): PlaceAddress?
}
