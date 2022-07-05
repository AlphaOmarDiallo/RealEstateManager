package com.example.realestatemanager.data.repositories.property

import com.example.realestatemanager.data.localData.PropertyDao
import com.example.realestatemanager.data.model.Property
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImp @Inject constructor(
    private val propertyDao: PropertyDao
) : PropertyRepository {

    val allProperties: Flow<List<Property>> = propertyDao.getListOfProperties()

    override suspend fun insertProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    override suspend fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }


}