package com.example.realestatemanager.data.repository.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import javax.inject.Inject

class LocationRepositoryImp @Inject constructor() : LocationRepository {

    private val officeLongitude = 40.741694549848404f
    private val officeLatitude = -73.98956985396345f
    private val locationRequestProviderInMs = 60000
    private val smallestDisplacementThresholdInMeter = 50f
    private val office = "Office"

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val locationMutableLiveData = MutableLiveData<Location>()

    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null


    override fun getCurrentLocation(): LiveData<Location?> {
        return locationMutableLiveData
    }

    override fun getOfficeLocation(): Location {
        val officeLocation = Location(office)
        officeLocation.longitude = officeLongitude.toDouble()
        officeLocation.latitude = officeLatitude.toDouble()
        return officeLocation
    }

    @SuppressLint("MissingPermission")
    override fun startLocationRequest(context: Context?, activity: Activity?) {
        instantiateFusedLocationProviderClient(context)
        getLastKnownLocation(activity)
        setupLocationRequest()
        createLocationCallback()
        startLocationUpdates()
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    private fun instantiateFusedLocationProviderClient(context: Context?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(activity: Activity?) {
        fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener(
                activity!!
            ) { location: Location? ->
                if (location != null) {
                    Log.i(ContentValues.TAG, "onSuccess: we got the last location", null)
                }
            }
    }

    private fun setupLocationRequest() {
        locationRequest = LocationRequest.create()
            .setInterval(locationRequestProviderInMs.toLong())
            .setSmallestDisplacement(smallestDisplacementThresholdInMeter)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    locationMutableLiveData.value = location
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}