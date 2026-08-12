package com.whereami.data.di

import com.whereami.core.time.SystemTimeProvider
import com.whereami.core.time.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeProviderModule {
    @Binds
    abstract fun bindTimeProvider(impl: SystemTimeProvider): TimeProvider
}
