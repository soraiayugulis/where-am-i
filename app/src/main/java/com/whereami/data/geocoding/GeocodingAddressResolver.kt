package com.whereami.data.geocoding

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.whereami.domain.model.Location
import com.whereami.domain.model.PlaceAddress
import com.whereami.domain.repository.AddressResolver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 250L
private const val TAG = "GeocodingAddressResolver"

class GeocodingAddressResolver @Inject constructor(
    @ApplicationContext private val context: Context
) : AddressResolver {

    @Suppress("DEPRECATION")
    override suspend fun resolve(location: Location): PlaceAddress? = withContext(Dispatchers.IO) {
        if (!Geocoder.isPresent()) return@withContext null

        val geocoder = Geocoder(context, Locale.getDefault())
        repeat(MAX_RETRIES) { attempt ->
            try {
                val addresses = geocoder.getFromLocation(location.lat, location.lng, 1)
                val address = addresses?.firstOrNull() ?: return@withContext null
                return@withContext PlaceAddress(
                    city = address.locality ?: address.subAdminArea,
                    state = address.adminArea,
                    country = address.countryName
                )
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
