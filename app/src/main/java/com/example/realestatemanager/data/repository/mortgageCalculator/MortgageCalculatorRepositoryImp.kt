package com.example.realestatemanager.data.repository.mortgageCalculator

import javax.inject.Inject
import kotlin.math.pow

class MortgageCalculatorRepositoryImp @Inject constructor() : MortgageCalculatorRepository {

    override fun monthlyPaymentMortgage(amount: Double, preferredRate: Double, years: Int): Int {
        val principal: Double = amount
        val rate: Double = (preferredRate / 100) / 12
        val time: Int = years * 12
        return (principal * rate / (1 - (1 + rate).pow(-time.toDouble()))).toInt()
    }

    override fun totalInvestmentCost(monthlyFee: Int, mortgageLength: Int): Int {
        return monthlyFee * (mortgageLength * 12)
    }

}

