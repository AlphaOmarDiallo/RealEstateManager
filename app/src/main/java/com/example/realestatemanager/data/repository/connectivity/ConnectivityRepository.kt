package com.example.realestatemanager.data.repository.connectivity

import android.content.Context

interface ConnectivityRepository {
    fun isInternetAvailable(context: Context): Boolean
}