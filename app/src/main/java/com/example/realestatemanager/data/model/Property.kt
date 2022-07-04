package com.example.realestatemanager.data.model

import android.graphics.Bitmap

data class Property(
    val id: Long,
    val type: String,
    val price: Int,
    val surface: Int,
    val numberOfRooms: Int,
    val description: String,
    val photos: List<Bitmap>,
    val address: String,
    val interestsAround: List<String>,
    val saleStatus: String,
    val onTheMarketSince: Long,
    val offTheMarketSince: Long?,
    val agentManagingPropertyId: Int
)
