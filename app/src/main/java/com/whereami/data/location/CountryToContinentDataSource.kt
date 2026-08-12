package com.whereami.data.location

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.whereami.domain.model.Continent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

interface CountryToContinentDataSource {
    fun getContinent(countryCode: String): Continent?
}

class AssetCountryToContinentDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CountryToContinentDataSource {

    private val map: Map<String, Continent> by lazy { loadMap() }

    private fun loadMap(): Map<String, Continent> {
        return try {
            val json = context.assets.open("country_to_continent.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<Map<String, Continent>>() {}.type
            Gson().fromJson<Map<String, Continent>>(json, type) ?: emptyMap()
        } catch (e: IOException) {
            emptyMap()
        }
    }

    override fun getContinent(countryCode: String): Continent? = map[countryCode]
}
