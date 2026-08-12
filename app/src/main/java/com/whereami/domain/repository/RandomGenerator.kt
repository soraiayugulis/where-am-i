package com.whereami.domain.repository

interface RandomGenerator {
    fun generate(seed: Int, bound: Int): Int
}
