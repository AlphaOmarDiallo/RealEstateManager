package com.example.realestatemanager.data.repositories.currencyAPI

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import retrofit2.Response

interface CurrencyAPIRepository {

    suspend  fun convertUSDtoEUR(): Response<UsdToEurRate>

    suspend fun convertEURtoUSD(): Response<EurToUsdRate>
}