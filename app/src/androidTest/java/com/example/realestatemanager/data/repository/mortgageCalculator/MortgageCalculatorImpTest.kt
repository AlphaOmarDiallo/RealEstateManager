package com.example.realestatemanager.data.repository.mortgageCalculator


import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MortgageCalculatorImpTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mortgageCalculatorRepository: MortgageCalculatorRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun mortgage_repository_is_not_null(){
        assertThat(mortgageCalculatorRepository).isNotNull()
    }

    @Test
    fun calculate_monthly_payment_mortgage_is_successful() {
        val principal = 300000.0
        val rate = 2.54
        val time = 30
        val expectedResult = 1191
        val unExpectedResult = 1000
        val actualResult = mortgageCalculatorRepository.monthlyPaymentMortgage(principal, rate, time)
        assertThat(actualResult).isEqualTo(expectedResult)
        assertThat(actualResult).isNotEqualTo(unExpectedResult)

    }

    @Test
    fun check_that_total_investment_in_calculated_correctly() {
        val payment = 1191
        val time = 30
        val expectedResult = (payment * (time * 12))
        val unexpectedResult = (payment * time) -1
        val actualResult = mortgageCalculatorRepository.totalInvestmentCost(payment, time)
        assertThat(actualResult).isEqualTo(expectedResult)
        assertThat(actualResult).isNotEqualTo(unexpectedResult)
    }
}