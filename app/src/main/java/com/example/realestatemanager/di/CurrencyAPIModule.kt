package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyAPIModule {

    @Singleton
    @Provides
    fun providesCurrencyAPIRepository(retrofitAbstractAPI: RetrofitAbstractAPI): CurrencyAPIRepository = CurrencyAPIRepositoryImp(retrofitAbstractAPI)
}