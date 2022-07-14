package com.example.realestatemanager.data.repositories.nearBySearch

import android.location.Location
import com.example.realestatemanager.data.model.nearBySearch.NearBySearch
import retrofit2.Response

interface NearBySearchRepository {

    suspend fun getInterestList(location: Location, pageToken: String?): Response<NearBySearch>
}