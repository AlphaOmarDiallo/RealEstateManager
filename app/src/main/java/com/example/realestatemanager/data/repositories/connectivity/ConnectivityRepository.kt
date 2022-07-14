package com.example.realestatemanager.data.repositories.connectivity

import android.content.Context
import androidx.lifecycle.LiveData

interface ConnectivityRepository {
    fun isInternetAvailable(context: Context): LiveData<Boolean>
}