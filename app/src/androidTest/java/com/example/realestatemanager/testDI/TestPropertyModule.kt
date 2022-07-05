package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.localData.PropertyDao
import com.example.realestatemanager.data.repositories.property.PropertyRepository
import com.example.realestatemanager.data.repositories.property.PropertyRepositoryImp
import com.example.realestatemanager.di.PropertyModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PropertyModule::class]
)

class TestPropertyModule {
    @Singleton
    @Provides
    fun providePropertyRepository(propertyDao: PropertyDao): PropertyRepository =
        PropertyRepositoryImp(propertyDao)
}