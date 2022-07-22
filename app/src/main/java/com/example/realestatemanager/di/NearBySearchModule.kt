package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepository
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NearBySearchModule {

    @Singleton
    @Provides
    fun provideNearBySearchRepository(retrofitGoogleAPI: RetrofitGoogleAPI): NearBySearchRepository =
        NearBySearchRepositoryImp(retrofitGoogleAPI)
}