package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepositoryImp
import com.example.realestatemanager.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)

class TestNetworkModule {

    var url = "http://127.0.0.1:8080"

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
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAbstractAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleServices(): RetrofitGoogleAPI {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitGoogleAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityService(): ConnectivityRepository = ConnectivityRepositoryImp()
}