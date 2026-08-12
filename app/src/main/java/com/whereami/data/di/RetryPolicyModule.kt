package com.whereami.data.di

import com.whereami.core.network.RetryPolicy
import com.whereami.data.network.RetryPolicyImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RetryPolicyModule {
    @Binds
    abstract fun bindRetryPolicy(impl: RetryPolicyImpl): RetryPolicy
}
