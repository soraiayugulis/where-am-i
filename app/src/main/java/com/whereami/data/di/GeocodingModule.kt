package com.whereami.data.di

import com.whereami.data.geocoding.GeocodingAddressResolver
import com.whereami.data.geocoding.GeocodingCountryResolver
import com.whereami.domain.repository.AddressResolver
import com.whereami.domain.repository.CountryResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GeocodingModule {
    @Binds
    abstract fun bindCountryResolver(impl: GeocodingCountryResolver): CountryResolver

    @Binds
    abstract fun bindAddressResolver(impl: GeocodingAddressResolver): AddressResolver
}
