package com.example.realestatemanager.di

import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepositoryImp
import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MortgageModule {

    @Singleton
    @Provides
    fun provideMortgageCalculatorRepository(): MortgageCalculatorRepository =
        MortgageCalculatorRepositoryImp()
}