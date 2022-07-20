package com.example.realestatemanager.domain

import kotlin.math.pow

object MortgagePaymentUtil {

    fun monthlyPaymentMortgage(amount: Double, preferredRate: Double, years: Int): Int {
        val principal: Double = amount
        val rate: Double = (preferredRate / 100) / 12;
        val time: Int = years * 12
        return (principal * rate / (1 - (1 + rate).pow(-time.toDouble()))).toInt()
    }

}