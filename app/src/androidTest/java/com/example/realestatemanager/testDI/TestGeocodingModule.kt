package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.geocoding.GeocodingRepository
import com.example.realestatemanager.data.repository.geocoding.GeocodingRepositoryImp
import com.example.realestatemanager.di.GeocodingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GeocodingModule::class]
)
class TestGeocodingModule {

    @Singleton
    @Provides
    fun provideGeocodingRepository(retrofitGoogleAPI: RetrofitGoogleAPI): GeocodingRepository =
        GeocodingRepositoryImp(retrofitGoogleAPI)
}