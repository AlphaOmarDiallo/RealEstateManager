package com.example.realestatemanager.data.repositories.currencyAPI

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import javax.inject.Inject

class CurrencyAPIRepositoryImp @Inject constructor(
    val retrofitAbstractAPI: RetrofitAbstractAPI
) : CurrencyAPIRepository {

    override suspend fun convertUSDtoEUR(): Response<UsdToEurRate> {
        return retrofitAbstractAPI.getUsdToEurConversionRate(
            Constant.ABSTRACT_API_KEY,
            Constant.USD,
            Constant.EUR
        )
    }

    override suspend fun convertEURtoUSD(): Response<EurToUsdRate> {
        return retrofitAbstractAPI.getEurToUsdConversionRate(
            Constant.ABSTRACT_API_KEY,
            Constant.EUR,
            Constant.USD
        )
    }

}