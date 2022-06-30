package com.example.realestatemanager.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.realestatemanager.R
import com.example.realestatemanager.databinding.ActivityMainBinding
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.domain.Utils
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private var connectivity: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkLocationPermission()

        checkConnectivity()
    }

    /**
     * Check connectivity
     */

    private fun checkConnectivity(): Boolean {
        return Utils.isInternetAvailable(this)
    }

    /**
     * Permission management
     */

    private fun checkLocationPermission() {
        if (hasRequiredPermissionsToCheckLocation()) Log.i(
            TAG,
            "checkLocationPermission: location permission granted"
        ) else requestPermissionsToCheckLocation()
    }

    private fun hasRequiredPermissionsToCheckLocation() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestPermissionsToCheckLocation() =
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.connectivity_rational),
            Constant.PERMISSION_CONNECTIVITY_REQUEST_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestPermissionsToCheckLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Utils.snackBarMaker(binding.root, "Location permissions granted")
        Utils.isInternetAvailableBuildVersionCodAboveM(this)
    }
}