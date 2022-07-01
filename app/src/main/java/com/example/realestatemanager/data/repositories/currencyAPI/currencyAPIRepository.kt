package com.example.realestatemanager.data.repositories.currencyAPI

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import retrofit2.Call

interface CurrencyAPIRepository {

    suspend  fun convertUSDtoEUR(): Call<UsdToEurRate>

    suspend fun convertEURtoUSD(): Call<EurToUsdRate>
}