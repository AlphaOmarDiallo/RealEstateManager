package com.example.realestatemanager.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */

    private val dollarToEuroRate = Constant.DOLLARS_TO_EURO
    private val euroToDollarRate = Constant.EURO_TO_DOLLARS
    private lateinit var networkRequest: NetworkRequest
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * dollarToEuroRate).roundToInt()
    }

    fun convertEuroToDollar(euro: Int): Int {
        return (euro * euroToDollarRate).roundToInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }

    fun isInternetAvailable2 (context: Context) {
        networkRequestCreation()
        networkCallBackCreation()
        connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
        Log.e("Utils", "isInternetAvailable2: ", )
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        Log.e("Utils", "isInternetAvailable2: " + connectivityManager.requestNetwork(networkRequest, networkCallback))
    }

    private fun networkRequestCreation() {
        networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private fun networkCallBackCreation () {
        networkCallback = object :  ConnectivityManager.NetworkCallback() {
             override fun onAvailable(network: Network) {
                 super.onAvailable(network)
             }

             override fun onCapabilitiesChanged(
                 network: Network,
                 networkCapabilities: NetworkCapabilities
             ) {
                 super.onCapabilitiesChanged(network, networkCapabilities)
             }

             override fun onLost(network: Network) {
                 super.onLost(network)
             }
        }
    }
}