package com.example.realestatemanager.data.remoteData

import com.example.realestatemanager.data.model.geocoding.Geocoding
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitGoogleAPI {

    @GET(Constant.GEOCODING_URL)
    suspend fun addressToLatLng(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<Geocoding>
}