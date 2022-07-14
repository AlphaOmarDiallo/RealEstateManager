package com.example.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "property_table")
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    val id: Int,
    @ColumnInfo(name = "property_type")
    val type: String,
    @ColumnInfo(name = "property_price")
    var price: Int,
    @ColumnInfo(name = "property_surface")
    val surface: Int,
    @ColumnInfo(name = "property_number_of_rooms")
    val numberOfRooms: Int,
    @ColumnInfo(name = "property_number_of_bedrooms")
    val numberOfBedrooms: Int,
    @ColumnInfo(name = "property_number_of_bathrooms")
    val numberOfBathrooms: Int,
    @ColumnInfo(name = "property_description")
    val description: String,
    @ColumnInfo(name = "property_photos")
    val photo: List<String>?,
    @ColumnInfo(name = "property_address")
    val address: String,
    @ColumnInfo(name = "property_city")
    val city: String,
    @ColumnInfo(name = "property_neighbourhood")
    val neighbourhood: String,
    @ColumnInfo(name = "property_interest_around")
    val interestsAround: List<String>?,
    @ColumnInfo(name = "property_sale_status")
    val saleStatus: String,
    @ColumnInfo(name = "property_on_market_since")
    val onTheMarketSince: Long,
    @ColumnInfo(name = "property_off_market_since")
    val offTheMarketSince: Long?,
    @ColumnInfo(name = "property_managing_agent")
    val agentManagingPropertyId: Int?
)
