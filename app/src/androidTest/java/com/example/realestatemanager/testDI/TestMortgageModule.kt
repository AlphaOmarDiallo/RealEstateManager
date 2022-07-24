package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepositoryImp
import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepository
import com.example.realestatemanager.di.MortgageModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MortgageModule::class]
)
class TestMortgageModule {

    @Singleton
    @Provides
    fun provideMortgageRepository(): MortgageCalculatorRepository =
        MortgageCalculatorRepositoryImp()
}