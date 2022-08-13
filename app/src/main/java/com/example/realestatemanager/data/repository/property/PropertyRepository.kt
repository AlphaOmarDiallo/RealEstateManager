package com.example.realestatemanager.data.repository.property

import android.database.Cursor
import com.example.realestatemanager.data.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun insertProperty(property: Property)

    suspend fun updateProperty(property: Property)

    fun getListOfProperties(): Flow<List<Property>>

    fun getProperty(propertyID: Int): Flow<Property>

    /**
     * Research
     */

    fun getPropertyResearch(
        isNearTypeProperty: List<String>,
        isNearCity: List<String>,
        isNearNeighbourhood: List<String>,
        isNearMinSurface: Int,
        isNearMaxSurface: Int,
        isNearSchool: List<Boolean>,
        isNearStore: List<Boolean>,
        isNearParc: List<Boolean>,
        isNearNumberOfPhotos: Int,
        isNearMinPrice: Int,
        isNearMaxPrice: Int,
        isNearSaleStatus: Boolean,
    ): Flow<List<Property>>


    /**
     * Content provider
     */

    fun getPropertyListWithCursor(): Cursor

    fun getPropertyWithCursor(id: Int): Cursor

}