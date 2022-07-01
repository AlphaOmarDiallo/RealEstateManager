package com.example.realestatemanager.data.repositories.currencyAPI

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.domain.RetrofitInstance
import retrofit2.Call
import javax.inject.Inject

class CurrencyAPIRepositoryImp @Inject constructor() : CurrencyAPIRepository {

    override suspend fun convertUSDtoEUR(): Call<UsdToEurRate> {
        return RetrofitInstance.api.getUsdToEurConversionRate(Constant.ABSTRACT_API_KEY, Constant.USD, Constant.EUR)
    }

    override suspend fun convertEURtoUSD(): Call<EurToUsdRate> {
        return RetrofitInstance.api.getEurToUsdConversionRate(Constant.ABSTRACT_API_KEY, Constant.EUR, Constant.USD)
    }

}