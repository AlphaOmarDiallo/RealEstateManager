package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.repositories.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repositories.connectivity.ConnectivityRepositoryImp
import com.example.realestatemanager.domain.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

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