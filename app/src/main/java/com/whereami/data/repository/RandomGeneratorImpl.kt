package com.whereami.data.repository

import com.whereami.domain.repository.RandomGenerator
import javax.inject.Inject
import kotlin.random.Random

class RandomGeneratorImpl @Inject constructor() : RandomGenerator {
    override fun generate(seed: Int, bound: Int): Int = Random(seed).nextInt(bound)
}
