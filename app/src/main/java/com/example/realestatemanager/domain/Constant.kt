package com.example.realestatemanager.domain

import com.example.realestatemanager.BuildConfig

object Constant {

    // Currency conversion
    const val DOLLARS_TO_EURO = 0.950
    const val EURO_TO_DOLLARS = 1.05

    //Date format
    const val DATE_FORMAT = "dd/MM/yyyy"

    //Permissions
    const val PERMISSION_CONNECTIVITY_REQUEST_CODE = 456

    //currencyAPI
    const val ABSTRACT_API_KEY = BuildConfig.ABSTRACT_API_KEY
    const val BASE_URL_CURRENCY_API = "https://exchange-rates.abstractapi.com/v1/"
    const val USD = "usd"
    const val EUR = "eur"

    //GoogleServices
    const val GOOGLE_API_KEY = BuildConfig.GOOGLE_CLOUD_API_KEY
    const val BASE_URL_GOOGLE_API = "https://maps.googleapis.com/"
    const val GEOCODING_URL = "maps/api/geocode/json?"
    const val NEARBYSEARCH_URL = "maps/api/place/nearbysearch/json"
    const val RADIUS_NEARBYSEARCH = 5000

}