package com.whereami.data.di

import com.whereami.data.repository.SeedRepositoryImpl
import com.whereami.data.seed.AssetSeedDataSource
import com.whereami.data.seed.SeedDataSource
import com.whereami.domain.repository.SeedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SeedModule {
    @Binds
    abstract fun bindSeedDataSource(impl: AssetSeedDataSource): SeedDataSource

    @Binds
    abstract fun bindSeedRepository(impl: SeedRepositoryImpl): SeedRepository
}
