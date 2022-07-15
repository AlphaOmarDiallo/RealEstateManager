package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepositoryImp
import com.example.realestatemanager.di.NetworkModule
import com.example.realestatemanager.domain.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)

class TestNetworkModule {

    @Singleton
    @Provides
    fun provideCurrencyService(): RetrofitAbstractAPI {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_CURRENCY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAbstractAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityService(): ConnectivityRepository = ConnectivityRepositoryImp()
}