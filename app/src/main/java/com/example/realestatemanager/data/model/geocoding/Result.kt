package com.example.realestatemanager.data.model.geocoding

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val partial_match: Boolean,
    val place_id: String,
    val types: List<String>
)