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




    /**
     * Content provider
     */

    fun getPropertyListWithCursor(): Cursor

    fun getPropertyWithCursor(id: Int): Cursor

}