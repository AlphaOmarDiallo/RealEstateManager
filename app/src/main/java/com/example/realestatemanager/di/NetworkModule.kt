package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepositoryImp
import com.example.realestatemanager.domain.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    @Singleton
    @Provides
    fun provideCurrencyService(): RetrofitAbstractAPI {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_CURRENCY_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAbstractAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleServices(): RetrofitGoogleAPI {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_GOOGLE_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitGoogleAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityService(): ConnectivityRepository = ConnectivityRepositoryImp()
}