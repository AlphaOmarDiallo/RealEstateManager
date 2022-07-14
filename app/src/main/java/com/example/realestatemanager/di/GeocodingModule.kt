package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repositories.geocoding.GeocodingRepository
import com.example.realestatemanager.data.repositories.geocoding.GeocodingRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GeocodingModule {

    @Singleton
    @Provides
    fun provideGeocodingRepository(retrofitGoogleAPI: RetrofitGoogleAPI): GeocodingRepository =
        GeocodingRepositoryImp(retrofitGoogleAPI)
}