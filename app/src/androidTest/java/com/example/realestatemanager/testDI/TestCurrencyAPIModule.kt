package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepositoryImp
import com.example.realestatemanager.di.CurrencyAPIModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CurrencyAPIModule::class]
)
class TestCurrencyAPIModule {
    @Singleton
    @Provides
    fun providesCurrencyAPIRepository(retrofitAbstractAPI: RetrofitAbstractAPI): CurrencyAPIRepository =
        CurrencyAPIRepositoryImp(retrofitAbstractAPI)
}