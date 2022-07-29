package com.example.realestatemanager.data.repository.property

import com.example.realestatemanager.data.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun insertProperty(property: Property)

    suspend fun updateProperty(property: Property)

    fun getListOfProperties(): Flow<List<Property>>

    fun getProperty(propertyID: Int): Flow<Property>

}