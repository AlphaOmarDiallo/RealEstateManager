package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.repository.geocoding.GeocodingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) : ViewModel() {

    val location: MutableState<Location?> = mutableStateOf(Location("location"))

    fun addressToLocation(address: String) {
        viewModelScope.launch {
            try {
                val response = geocodingRepository.convertAddressToGeocode(address)

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "address to location: no response from API", null)
                    return@launch
                }

                if (response.body() != null) {
                    Log.i(
                        ContentValues.TAG,
                        "address to location" + response.body()?.results?.get(0)!!.geometry.location
                    )

                    val tempLoc = Location("TempLoc")
                    tempLoc.latitude = response.body()?.results?.get(0)!!.geometry.location.lat
                    tempLoc.longitude = response.body()?.results?.get(0)!!.geometry.location.lng
                    location.value = tempLoc

                } else {
                    Log.e(ContentValues.TAG, "address to location: null data", null)
                }
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "address to location" + e.message, null)
            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "address to location" + e.message(), null)
            }
        }
    }
}