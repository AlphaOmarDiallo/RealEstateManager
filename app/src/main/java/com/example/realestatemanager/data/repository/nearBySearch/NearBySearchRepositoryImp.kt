package com.example.realestatemanager.data.repository.nearBySearch

import android.location.Location
import com.example.realestatemanager.data.model.nearBySearch.NearBySearch
import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import javax.inject.Inject

class NearBySearchRepositoryImp @Inject constructor(
    private val retrofitGoogleAPI: RetrofitGoogleAPI
) : NearBySearchRepository {

    override suspend fun getInterestList(
        location: Location,
        pageToken: String?
    ): Response<NearBySearch> {
        return retrofitGoogleAPI.interestsAround(
            "${location.latitude},${location.longitude}",
            Constant.RADIUS_NEARBYSEARCH,
            Constant.GOOGLE_API_KEY,
            pageToken
        )
    }


}