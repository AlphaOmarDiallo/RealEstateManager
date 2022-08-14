package com.example.realestatemanager.data.model

import java.io.Serializable

data class SearchResultObject(
    val result: List<Property>
) : Serializable