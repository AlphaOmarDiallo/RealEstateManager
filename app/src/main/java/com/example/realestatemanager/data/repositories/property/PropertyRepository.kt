package com.example.realestatemanager.data.repositories.property

import com.example.realestatemanager.data.model.Property

interface PropertyRepository {

    suspend fun insertProperty(property: Property)

    suspend fun updateProperty(property: Property)

}