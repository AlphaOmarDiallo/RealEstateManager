package com.example.realestatemanager.data.viewmodel

import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.repository.location.LocationRepository
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    /**
     * Location repository
     */

    fun getOfficeLocation(): Location? = locationRepository.getOfficeLocation()

    fun startLocationTracking(activity: Activity, context: Context) =
        locationRepository.startLocationRequest(context, activity)

    fun getCurrentLocation(): LiveData<Location?>? = locationRepository.getCurrentLocation()

    /**
     * Property repository
     */

    private val _propertyList: MutableLiveData<List<Property>> = MutableLiveData()
    val propertyList: LiveData<List<Property>> get() = _propertyList

    fun getProperties() {
        viewModelScope.launch {
            _propertyList.value = propertyRepository.getListOfProperties().first()
        }
    }

}