package com.example.realestatemanager.data.model.geocoding

data class Geocoding(
    val results: List<Result>,
    val status: String
)