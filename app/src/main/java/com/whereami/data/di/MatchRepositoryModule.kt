package com.whereami.data.di

import com.whereami.data.repository.MatchRepositoryImpl
import com.whereami.domain.repository.MatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MatchRepositoryModule {
    @Binds
    abstract fun bindMatchRepository(impl: MatchRepositoryImpl): MatchRepository
}
