package com.whereami.data.di

import com.whereami.core.time.Clock
import com.whereami.core.time.SystemClock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeModule {
    @Binds
    abstract fun bindClock(impl: SystemClock): Clock
}
