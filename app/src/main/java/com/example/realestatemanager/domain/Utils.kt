package com.example.realestatemanager.domain

import android.content.Context
import android.net.wifi.WifiManager
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
}