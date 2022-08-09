package com.example.realestatemanager.data.repository.permission

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface PermissionRepository {

    fun hasLocationPermission(context: Context): Flow<Boolean>

}