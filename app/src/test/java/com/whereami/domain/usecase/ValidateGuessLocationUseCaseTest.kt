package com.whereami.domain.usecase

import com.whereami.core.location.ContinentResolver
import com.whereami.domain.model.Continent
import com.whereami.domain.model.Location
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateGuessLocationUseCaseTest {
    private val southAmerica = Continent.SOUTH_AMERICA

    @Test
    fun `valid country resolves to continent`() = runTest {
        val useCase = ValidateGuessLocationUseCase(FakeContinentResolver(southAmerica))
        val result = useCase(Location(-23.55, -46.63))
        assertTrue(result.isSuccess)
        assertEquals(southAmerica, result.getOrThrow())
    }

    @Test
    fun `ocean is rejected`() = runTest {
        val useCase = ValidateGuessLocationUseCase(FakeContinentResolver(null))
        val result = useCase(Location(0.0, 0.0))
        assertTrue(result.isFailure)
    }

    private class FakeContinentResolver(private val continent: Continent?) : ContinentResolver {
        override suspend fun resolve(location: Location): Continent? = continent
    }
}
