package com.whereami.domain.model

data class PlaceAddress(
    val city: String?,
    val state: String?,
    val country: String?
) {
    fun format(): String =
        listOfNotNull(city, state, country)
            .filter { it.isNotBlank() }
            .joinToString(", ")
            .ifBlank { "Unknown location" }
}
