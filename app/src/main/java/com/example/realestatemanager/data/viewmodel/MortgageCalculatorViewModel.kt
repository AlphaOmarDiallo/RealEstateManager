package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.repository.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repository.mortgageCalculator.MortgageCalculatorRepository
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MortgageCalculatorViewModel @Inject constructor(
    private val mortgageCalculatorRepository: MortgageCalculatorRepository,
    private val currencyAPIRepository: CurrencyAPIRepository
) : ViewModel() {

    init {
        getEurRate()
        getUsdRate()
    }

    private val _usdRate: MutableLiveData<Double> = MutableLiveData()
    val usdRate: LiveData<Double> get() = _usdRate

    private val _eurRate: MutableLiveData<Double> = MutableLiveData()
    val eurRate: LiveData<Double> get() = _eurRate

    private val _monthlyFee: MutableLiveData<Int> = MutableLiveData()
    val monthlyFee: LiveData<Int> get() = _monthlyFee

    @JvmName("getUsdRate1")
    fun getUsdRate() {
        viewModelScope.launch {

            try {
                val response = currencyAPIRepository.convertEURtoUSD()

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "getUsdRate: no response from API", null)
                    _usdRate.value = Constant.EURO_TO_DOLLARS
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(ContentValues.TAG, "exchange rate " + response.body()?.exchange_rates)
                    _usdRate.value = response.body()?.exchange_rates!!.USD
                } else {
                    Log.e(ContentValues.TAG, "getUsdRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "getUsdRate: IOException" + e.message, null)
                _usdRate.value = Constant.EURO_TO_DOLLARS
            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "getUsdRate: HttpException" + e.message(), null)
                _usdRate.value = Constant.EURO_TO_DOLLARS
            }
        }
    }

    fun getEurRate() {
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertUSDtoEUR()

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "getEurRate: no response from API", null)
                    _eurRate.value = Constant.DOLLARS_TO_EURO
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(ContentValues.TAG, "exchange rate " + response.body()?.exchange_rates)
                    _eurRate.value = response.body()?.exchange_rates!!.EUR
                } else {
                    Log.e(ContentValues.TAG, "getEurRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "getEurRate: IOException" + e.message, null)
                _eurRate.value = Constant.DOLLARS_TO_EURO
            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "getEurRate: HttpException" + e.message(), null)
                _eurRate.value = Constant.DOLLARS_TO_EURO
            }
        }
    }

    fun getMortgageMonthlyPaymentFee(amount: Double, preferredRate: Double, years: Int) {
        viewModelScope.launch {
            _monthlyFee.value =
                mortgageCalculatorRepository.monthlyPaymentMortgage(amount, preferredRate, years)
        }
    }

}