package com.whereami.domain.model

import java.util.Locale

data class Location(val lat: Double, val lng: Double) {
    fun toLatLngString(): String =
        String.format(Locale.US, "%.5f,%.5f", lat, lng)
}
