package com.example.realestatemanager.data.viewmodel

import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.realestatemanager.data.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    init {

    }

    fun getOfficeLocation(): Location? = locationRepository.getOfficeLocation()

    fun startLocationTracking(activity: Activity, context: Context) = locationRepository.startLocationRequest(context,activity)

    fun getCurrentLocation(): LiveData<Location?>? = locationRepository.getCurrentLocation()
}