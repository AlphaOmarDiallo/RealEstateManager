package com.example.realestatemanager.data.repository.location

import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData

interface LocationRepository {

    fun getCurrentLocation(): LiveData<Location?>?

    fun startLocationRequest(context: Context?, activity: Activity?)

    fun getOfficeLocation(): Location?

    fun stopLocationUpdates()

}