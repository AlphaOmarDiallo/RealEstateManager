package com.example.realestatemanager.data.repository.connectivity

import android.content.Context
import androidx.lifecycle.LiveData

interface ConnectivityRepository {
    fun isInternetAvailable(context: Context): LiveData<Boolean>
}