package com.example.realestatemanager.data.repository.geocoding

import com.example.realestatemanager.data.model.geocoding.Geocoding
import retrofit2.Response

interface GeocodingRepository {

    suspend fun convertAddressToGeocode(address: String): Response<Geocoding>
}