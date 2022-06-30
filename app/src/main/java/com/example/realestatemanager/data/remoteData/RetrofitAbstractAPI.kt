package com.example.realestatemanager.data.remoteData

import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAbstractAPI{
    @GET("/live/")
    fun getEurToUsdConversionRate(
        @Query("api_key") apiKey: String,
        @Query("base") base: String,
        @Query("target") target: String
    ): Call<EurToUsdRate>


}