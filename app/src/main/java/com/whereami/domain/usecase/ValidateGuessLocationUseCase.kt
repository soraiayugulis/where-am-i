package com.whereami.domain.usecase

import com.whereami.core.location.ContinentResolver
import com.whereami.domain.model.Continent
import com.whereami.domain.model.Location
import javax.inject.Inject

class ValidateGuessLocationUseCase @Inject constructor(
    private val continentResolver: ContinentResolver
) {
    suspend operator fun invoke(location: Location): Result<Continent> = runCatching {
        continentResolver.resolve(location)
            ?: throw IllegalArgumentException("Location is not on a recognized continent")
    }
}
