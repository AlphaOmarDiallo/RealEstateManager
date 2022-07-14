package com.example.realestatemanager.data.repositories.geocoding

import com.example.realestatemanager.data.model.geocoding.Geocoding
import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import javax.inject.Inject

class GeocodingRepositoryImp @Inject constructor(
    private val retrofitGoogleAPI: RetrofitGoogleAPI
) : GeocodingRepository {

    override suspend fun convertAddressToGeocode(address: String): Response<Geocoding> {
        return retrofitGoogleAPI.addressToLatLng(
            address,
            Constant.GOOGLE_API_KEY
        )
    }


}