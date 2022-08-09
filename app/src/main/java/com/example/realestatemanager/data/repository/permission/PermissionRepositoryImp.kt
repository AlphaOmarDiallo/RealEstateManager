package com.example.realestatemanager.data.repository.permission

import android.Manifest
import android.content.Context
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PermissionRepositoryImp @Inject constructor() : PermissionRepository {
    override fun hasLocationPermission(context: Context): Flow<Boolean> {
        return flow {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

    }
}