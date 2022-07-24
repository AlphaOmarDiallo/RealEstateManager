package com.example.realestatemanager.data.repository.mortgageCalculator

interface MortgageCalculatorRepository {

    fun monthlyPaymentMortgage(amount: Double, preferredRate: Double, years: Int): Int

    fun totalInvestmentCost(monthlyFee: Int, mortgageLength: Int): Int

}