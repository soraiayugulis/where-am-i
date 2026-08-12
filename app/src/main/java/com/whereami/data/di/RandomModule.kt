package com.whereami.data.di

import com.whereami.data.repository.RandomGeneratorImpl
import com.whereami.domain.repository.RandomGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RandomModule {
    @Binds
    abstract fun bindRandomGenerator(impl: RandomGeneratorImpl): RandomGenerator
}
