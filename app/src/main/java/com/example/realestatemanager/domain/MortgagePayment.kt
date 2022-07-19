package com.example.realestatemanager.domain

import kotlin.math.pow

object MortgagePayment {

    fun monthlyPaymentMortgage(amount: Double, preferredRate: Double, month: Int): Int {
        val principal: Double = amount
        val rate: Double = (preferredRate / 100) / 12;
        val time: Int = month * 12
        return (principal * rate / (1 - (1 + rate).pow(-time.toDouble()))).toInt()
    }

}