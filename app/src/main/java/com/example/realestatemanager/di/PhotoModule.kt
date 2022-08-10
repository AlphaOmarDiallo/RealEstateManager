package com.example.realestatemanager.di

import com.example.realestatemanager.data.localData.PhotoDao
import com.example.realestatemanager.data.repository.photo.PhotoRepository
import com.example.realestatemanager.data.repository.photo.PhotoRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PhotoModule {

    @Singleton
    @Provides
    fun providePhotoRepository(photoDao: PhotoDao): PhotoRepository = PhotoRepositoryImp(photoDao)
}