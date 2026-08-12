package com.whereami.core.location

import com.whereami.domain.model.Location
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_KM = 6371.0

class DistanceCalculator @Inject constructor() {
    fun distanceInKm(a: Location, b: Location): Double {
        val dLat = Math.toRadians(b.lat - a.lat)
        val dLon = Math.toRadians(b.lng - a.lng)
        val lat1 = Math.toRadians(a.lat)
        val lat2 = Math.toRadians(b.lat)

        val h = sin(dLat / 2).pow(2) +
            cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(h), sqrt(1 - h))

        return EARTH_RADIUS_KM * c
    }
}
