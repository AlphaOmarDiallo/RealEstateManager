package com.example.realestatemanager.domain

import com.example.realestatemanager.BuildConfig

object Constant {

    // Currency conversion
    const val DOLLARS_TO_EURO = 0.967
    const val EURO_TO_DOLLARS = 1.03

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
    const val GOOGLE_API_KEY = BuildConfig.MAPS_API_KEY
    const val BASE_URL_GOOGLE_API = "https://maps.googleapis.com/maps/api/"

    const val GEOCODING_URL = "geocode/json?"

    const val NEARBYSEARCH_URL = "place/nearbysearch/json"
    const val RADIUS_NEARBYSEARCH = 3000

    const val AUTOCOMPLETE_COUNTRY = "US"

    const val SCHOOL = "school"
    const val S_SCHOOL = "secondary_school"
    const val P_SCHOOL = "primary_school"
    const val SHOPS = "supermarket"
    const val TRANSPORT = "train_station"
    const val PARK = "park"

    //Preferences Datastore
    const val APP_PREFERENCES = "app_preferences"

}