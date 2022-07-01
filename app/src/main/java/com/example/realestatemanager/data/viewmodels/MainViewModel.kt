package com.example.realestatemanager.data.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.eurToUsd.ExchangeRates
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
 class MainViewModel @Inject constructor(
    val currencyAPIRepository: CurrencyAPIRepository
) : ViewModel() {

    val usdRate: MutableLiveData<ExchangeRates> = MutableLiveData()

    @JvmName("getUsdRate1")
    fun getUsdRate(){
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertEURtoUSD()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getUsdRate: no response from API", null)
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(TAG, "exchange rate " + response.body()?.exchange_rates)
                    usdRate.value = response.body()?.exchange_rates
                } else {
                    print("null data")
                }

                Log.i(TAG, "getUsdRate: " + response.raw().request.url)
                Log.i(TAG, "getUsdRate: " + response.body()?.exchange_rates?.USD.toString())

            } catch (e: IOException) {
                Log.e(TAG, "getUsdRate: IOException" + e.message, null)
            } catch (e: HttpException) {
                Log.e(TAG, "getUsdRate: HttpException" + e.message(), null)
            }
        }
    }

}