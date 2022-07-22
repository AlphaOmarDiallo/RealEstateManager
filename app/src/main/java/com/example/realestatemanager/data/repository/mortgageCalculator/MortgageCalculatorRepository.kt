package com.example.realestatemanager.data.repository.mortgageCalculator

interface MortgageCalculatorRepository {

    suspend fun monthlyPaymentMortgage(amount: Double, preferredRate: Double, years: Int): Int

}