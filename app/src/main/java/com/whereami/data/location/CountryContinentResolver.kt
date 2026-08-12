package com.whereami.data.location

import com.whereami.core.location.ContinentResolver
import com.whereami.domain.model.Continent
import com.whereami.domain.model.Location
import com.whereami.domain.repository.CountryResolver
import javax.inject.Inject

class CountryContinentResolver @Inject constructor(
    private val countryResolver: CountryResolver,
    private val dataSource: CountryToContinentDataSource
) : ContinentResolver {
    override suspend fun resolve(location: Location): Continent? {
        val country = countryResolver.resolve(location) ?: return null
        return dataSource.getContinent(country.code)
    }
}
