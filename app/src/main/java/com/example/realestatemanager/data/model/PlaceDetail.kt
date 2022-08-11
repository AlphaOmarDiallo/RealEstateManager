package com.example.realestatemanager.data.model

data class PlaceDetail(
    val placeID: String,
    val placeName: String,
    val placeLat: String,
    val placeLng: String,
    val placeWebSiteUri: String?,
    val placeType: String
)