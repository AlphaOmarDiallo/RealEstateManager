package com.example.realestatemanager.data.remoteData

import com.example.realestatemanager.data.model.autocomplete.Autocomplete
import com.example.realestatemanager.data.model.geocoding.Geocoding
import com.example.realestatemanager.data.model.nearBySearch.NearBySearch
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.annotation.Nullable

interface RetrofitGoogleAPI {

    @GET(Constant.GEOCODING_URL)
    suspend fun addressToLatLng(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<Geocoding>

    @GET(Constant.NEARBYSEARCH_URL)
    suspend fun interestsAround(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") apiKey: String,
        @Nullable @Query("pagetoken") pageToken: String?
    ): Response<NearBySearch>

    @GET(Constant.AUTOCOMPLETE_URL)
    suspend fun autocompleteAddress(
        @Query("input") input: String,
        @Query("components") components: String,
        @Query("offset") offset: Int,
        @Query("types") types: String,
        @Query("key") key: String
    ): Response<Autocomplete>
}