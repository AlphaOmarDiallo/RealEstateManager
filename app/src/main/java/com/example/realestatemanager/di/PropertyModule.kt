package com.example.realestatemanager.di

import com.example.realestatemanager.data.localData.PropertyDao
import com.example.realestatemanager.data.repository.property.PropertyRepository
import com.example.realestatemanager.data.repository.property.PropertyRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PropertyModule {

    @Singleton
    @Provides
    fun providePropertyRepository(propertyDao: PropertyDao): PropertyRepository =
        PropertyRepositoryImp(propertyDao)

}