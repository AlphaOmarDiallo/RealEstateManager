package com.example.realestatemanager.data.remoteData

import com.example.realestatemanager.data.model.nearBySearch.NearBySearch
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.annotation.Nullable

interface RetrofitGoogleAPI {

    @GET(Constant.NEARBYSEARCH_URL)
    suspend fun interestsAround(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") apiKey: String,
        @Nullable @Query("pagetoken") pageToken: String?
    ): Response<NearBySearch>

}