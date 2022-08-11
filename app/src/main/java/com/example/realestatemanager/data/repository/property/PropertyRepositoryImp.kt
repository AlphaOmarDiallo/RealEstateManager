package com.example.realestatemanager.data.repository.property

import android.database.Cursor
import com.example.realestatemanager.data.localData.PropertyDao
import com.example.realestatemanager.data.model.Property
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImp @Inject constructor(
    private val propertyDao: PropertyDao
) : PropertyRepository {

    override suspend fun insertProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    override suspend fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }

    override fun getListOfProperties(): Flow<List<Property>> = propertyDao.getListOfProperties()

    override fun getProperty(propertyID: Int): Flow<Property> = propertyDao.getProperty(propertyID)

    /**
     * Content provider
     */
    override fun getPropertyListWithCursor(): Cursor {
        return propertyDao.getPropertyListWithCursor()
    }

    override fun getPropertyWithCursor(id: Int): Cursor {
        return propertyDao.getPropertyWithCursor(id)
    }


}