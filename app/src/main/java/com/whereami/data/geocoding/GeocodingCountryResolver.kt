package com.whereami.data.geocoding

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.whereami.domain.model.Country
import com.whereami.domain.model.Location
import com.whereami.domain.repository.CountryResolver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 250L
private const val TAG = "GeocodingCountryResolver"

class GeocodingCountryResolver @Inject constructor(
    @ApplicationContext private val context: Context
) : CountryResolver {

    @Suppress("DEPRECATION")
    override suspend fun resolve(location: Location): Country? = withContext(Dispatchers.IO) {
        if (!Geocoder.isPresent()) return@withContext null

        val geocoder = Geocoder(context, Locale.getDefault())
        repeat(MAX_RETRIES) { attempt ->
            try {
                val addresses = geocoder.getFromLocation(location.lat, location.lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val code = address.countryCode
                    val name = address.countryName
                    if (!code.isNullOrBlank() && !name.isNullOrBlank()) {
                        return@withContext Country(code, name)
                    }
                }
                return@withContext null
            } catch (e: IOException) {
                if (attempt == MAX_RETRIES - 1) {
                    Log.e(TAG, "Geocoding failed after $MAX_RETRIES attempts", e)
                    return@withContext null
                }
                delay(RETRY_DELAY_MS)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid coordinates for geocoding: $location", e)
                return@withContext null
            }
        }
        null
    }
}
