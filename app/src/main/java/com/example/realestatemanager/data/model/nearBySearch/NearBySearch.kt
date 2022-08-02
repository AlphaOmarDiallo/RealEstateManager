package com.example.realestatemanager.data.model.nearBySearch

data class NearBySearch(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val places: List<Place>,
    val status: String
)