package com.whereami.data.di

import com.whereami.core.location.ContinentResolver
import com.whereami.data.location.AssetCountryToContinentDataSource
import com.whereami.data.location.CountryContinentResolver
import com.whereami.data.location.CountryToContinentDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ContinentModule {
    @Binds
    abstract fun bindCountryToContinentDataSource(impl: AssetCountryToContinentDataSource): CountryToContinentDataSource

    @Binds
    abstract fun bindContinentResolver(impl: CountryContinentResolver): ContinentResolver
}
