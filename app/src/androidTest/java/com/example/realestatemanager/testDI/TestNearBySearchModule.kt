package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepository
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepositoryImp
import com.example.realestatemanager.di.NearBySearchModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NearBySearchModule::class]
)
class TestNearBySearchModule {

    @Singleton
    @Provides
    fun provideNearBySearchRepository(retrofitGoogleAPI: RetrofitGoogleAPI): NearBySearchRepository =
        NearBySearchRepositoryImp(retrofitGoogleAPI)
}