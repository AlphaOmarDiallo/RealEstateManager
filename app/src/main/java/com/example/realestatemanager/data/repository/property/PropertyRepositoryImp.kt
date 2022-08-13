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
     * Research function
     */
    override fun getPropertyResearch(
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
    ): Flow<List<Property>> {
        return propertyDao.getPropertyResearch(
            isNearTypeProperty,
            isNearCity,
            isNearNeighbourhood,
            isNearMinSurface,
            isNearMaxSurface,
            isNearSchool,
            isNearStore,
            isNearParc,
            isNearNumberOfPhotos,
            isNearMinPrice,
            isNearMaxPrice,
            isNearSaleStatus
        )
    }


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