package com.example.realestatemanager.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "property_table")
data class Property(
    @ColumnInfo(name = "property_type")
    var type: String,
    @ColumnInfo(name = "property_price")
    var price: Int,
    @ColumnInfo(name = "property_surface")
    var surface: Int,
    @ColumnInfo(name = "property_number_of_rooms")
    var numberOfRooms: Int,
    @ColumnInfo(name = "property_number_of_bedrooms")
    var numberOfBedrooms: Int,
    @ColumnInfo(name = "property_number_of_bathrooms")
    var numberOfBathrooms: Int,
    @ColumnInfo(name = "property_description")
    var description: String,
    @ColumnInfo(name = "property_photos")
    var photo: List<Bitmap>,
    @ColumnInfo(name = "property_address")
    var address: String,
    @ColumnInfo(name = "property_city")
    var city: String,
    @ColumnInfo(name = "property_neighbourhood")
    var neighbourhood: String,
    @ColumnInfo(name = "property_sale_status")
    var saleStatus: Boolean,
    @ColumnInfo(name = "property_on_market_since")
    val onTheMarketSince: Long,
    @ColumnInfo(name = "property_off_market_since")
    var offTheMarketSince: Long?,
    @ColumnInfo(name = "property_managing_agent")
    var agentManagingPropertyId: String?,
    @ColumnInfo(name = "property_list_of_interest")
    var listOfInterest: List<String>?,
    @ColumnInfo(name = "property_close_school")
    var closeToSchool: Boolean,
    @ColumnInfo(name = "property_close_shops")
    var closeToShops: Boolean,
    @ColumnInfo(name = "property_close_park")
    var closeToPark: Boolean,
    @ColumnInfo(name = "property_close_transport")
    var closeToTransport: Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    var id: Int = 0
}