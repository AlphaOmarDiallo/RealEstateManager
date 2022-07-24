package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.repository.dataStore.DataStoreRepository
import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepository
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MortgageCalculatorViewModel @Inject constructor(
    private val mortgageCalculatorRepository: MortgageCalculatorRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _usdToEurRate: MutableLiveData<Double> = MutableLiveData(Constant.DOLLARS_TO_EURO)
    val usdToEurRate: LiveData<Double> get() = _usdToEurRate

    private val _eurToDollarRate: MutableLiveData<Double> = MutableLiveData(Constant.EURO_TO_DOLLARS)
    val eurToDollarRate: LiveData<Double> get() = _eurToDollarRate

    /**
     * DataStore repository
     */

    fun readCurrencies() {
        readEuroToDollarRateFromDataStore()
        readDollarToEuroRateFromDataStore()
    }

    private fun readEuroToDollarRateFromDataStore() {
        viewModelScope.launch {
            dataStoreRepository.readEuroToDollarRate().collect {
                _eurToDollarRate.value = it
                Log.d(TAG, "readEuroToDollarRateFromDataStore: ${_eurToDollarRate.value}")
            }
        }
    }

    private fun readDollarToEuroRateFromDataStore() {
        viewModelScope.launch {
            dataStoreRepository.readDollarToEuroRate().collect {
                _usdToEurRate.value = it
            }
        }
    }

    /**
     * Mortgage payment repository
     */

    fun getMortgageMonthlyPaymentFee(amount: Double, preferredRate: Double, years: Int): Int {
        return mortgageCalculatorRepository.monthlyPaymentMortgage(amount, preferredRate, years)
    }

}