package com.example.realestatemanager.data.remoteData

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAbstractAPI{
    @GET("live/")
    suspend fun getEurToUsdConversionRate(
        @Query("api_key") apiKey: String,
        @Query("base") base: String,
        @Query("target") target: String
    ): Response<EurToUsdRate>

    @GET("live/")
    suspend fun getUsdToEurConversionRate(
        @Query("api_key") apiKey: String,
        @Query("base") base: String,
        @Query("target") target: String
    ): Response<UsdToEurRate>
}