package com.whereami.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class RandomGeneratorImplTest {
    @Test
    fun `same seed produces same index`() {
        val generator = RandomGeneratorImpl()
        assertEquals(generator.generate(123, 1000), generator.generate(123, 1000))
    }
}
