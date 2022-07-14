package com.example.realestatemanager.data.repositories.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException

class ConnectivityRepositoryImp : ConnectivityRepository {

    private var connected: MutableLiveData<Boolean> = MutableLiveData()

    override fun isInternetAvailable(context: Context): LiveData<Boolean> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isInternetAvailableBuildVersionCodAboveM(context)
        } else {
            isInternetAvailableBuildVersionBelowM()
        }
    }

    private fun isInternetAvailableBuildVersionBelowM(): LiveData<Boolean> {
        val ping = "/system/bin/ping -c 1 8.8.8.8"
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec(ping)
            val exitValue = ipProcess.waitFor()
            connected.value = exitValue == 0
            return (connected)
        } catch (exception: IOException) {
            exception.printStackTrace()
            connected.value = false
        }
        return connected


    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun isInternetAvailableBuildVersionCodAboveM(context: Context): LiveData<Boolean> {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    connected.value = true
                    return connected
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    connected.value = true
                    return connected
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    connected.value = true
                    return connected
                }
            }
        }
        connected.value = false
        return connected
    }
}