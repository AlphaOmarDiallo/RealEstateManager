package com.example.realestatemanager.data.repositories.connectivity

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    fun isInternetAvailable(context: Context): LiveData<Boolean>
}