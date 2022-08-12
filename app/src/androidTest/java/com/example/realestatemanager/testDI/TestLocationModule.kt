package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.repository.location.LocationRepository
import com.example.realestatemanager.data.repository.location.LocationRepositoryImp
import com.example.realestatemanager.di.LocationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocationModule::class]
)
class TestLocationModule {

    @Singleton
    @Provides
    fun provideLocationRepository(): LocationRepository = LocationRepositoryImp()
}