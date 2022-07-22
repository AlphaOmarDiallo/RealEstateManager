package com.example.realestatemanager.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MortgagePaymentUtilTest{

    @Test
    fun calculate_monthly_payment_mortgage_is_successful() {
        val principal = 300000.0
        val rate = 2.54
        val time = 30
        val expectedResult = 1191
        val actualResult = MortgagePaymentUtil.monthlyPaymentMortgage(principal, rate, time)
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun calculate_monthly_payment_mortgage_is_not_successful() {
        val principal = 300000.0
        val rate = 2.54
        val time = 30
        val expectedResult = 1000
        val actualResult = MortgagePaymentUtil.monthlyPaymentMortgage(principal, rate, time)
        assertThat(actualResult).isNotEqualTo(expectedResult)
    }
}