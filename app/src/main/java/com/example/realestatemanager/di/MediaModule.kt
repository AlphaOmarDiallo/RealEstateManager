package com.example.realestatemanager.di

import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import com.example.realestatemanager.data.repository.media.MediaStoreRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MediaModule {

    @Singleton
    @Provides
    fun provideMediaRepository(): MediaStoreRepository =
        MediaStoreRepositoryImp()
}