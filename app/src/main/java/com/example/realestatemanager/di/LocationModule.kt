package com.example.realestatemanager.di

import com.example.realestatemanager.data.repository.location.LocationRepository
import com.example.realestatemanager.data.repository.location.LocationRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationModule {

    @Singleton
    @Provides
    fun provideLocationRepository(): LocationRepository = LocationRepositoryImp()
}