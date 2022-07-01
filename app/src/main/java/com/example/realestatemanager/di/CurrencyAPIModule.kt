package com.example.realestatemanager.di

import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CurrencyAPIModule {
    @Provides
    fun providesCurrencyAPIRepository(): CurrencyAPIRepository = CurrencyAPIRepositoryImp()
}