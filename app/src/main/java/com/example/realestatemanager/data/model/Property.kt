package com.example.realestatemanager.data.model

data class Property(
    val id: Long,
    val type: String,
    val price: Int,
    val surface: Int,
    val numberOfRooms: Int,
    val description: String,
    val photos: List<Int>,
    val address: String,
    val interestsAround: List<String>,
    val saleStatus: String,
    val onTheMarketSince: Long,
    val offTheMarketSince:Long?,
    val agentManagingProperty: Agent
)
