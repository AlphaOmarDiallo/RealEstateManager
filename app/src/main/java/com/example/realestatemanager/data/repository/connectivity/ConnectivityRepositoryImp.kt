package com.example.realestatemanager.data.repository.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException

class ConnectivityRepositoryImp : ConnectivityRepository {

    private var connected: Boolean = false

    override fun isInternetAvailable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isInternetAvailableBuildVersionCodAboveM(context)
        } else {
            isInternetAvailableBuildVersionBelowM()
        }
    }

    private fun isInternetAvailableBuildVersionBelowM(): Boolean {
        val ping = "/system/bin/ping -c 1 8.8.8.8"
        val runtime = Runtime.getRuntime()
        connected = try {
            val ipProcess = runtime.exec(ping)
            val exitValue = ipProcess.waitFor()
            exitValue == 0
        } catch (exception: IOException) {
            exception.printStackTrace()
            false
        }
        return connected
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun isInternetAvailableBuildVersionCodAboveM(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    connected = true
                    return connected
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    connected = true
                    return connected
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    connected = true
                    return connected
                }
            }
        }
        connected = false
        return connected
    }
}