package com.example.realestatemanager.data.localData

import android.database.Cursor
import androidx.room.*
import com.example.realestatemanager.data.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Update
    suspend fun updateProperty(property: Property)

    @Query("SELECT * FROM property_table")
    fun getListOfProperties(): Flow<List<Property>>

    @Query("SELECT * FROM property_table WHERE property_id= :id")
    fun getProperty(id: Int): Flow<Property>

    /**
     * SEARCH MULTI CRITERIA
     */
    @Query(
        "SELECT * FROM property_table WHERE property_type IN (:isNearTypeProperty) " +
                "AND property_city IN (:isNearCity)" +
                "AND property_neighbourhood IN (:isNearNeighbourhood)" +
                "AND property_surface BETWEEN :isNearMinSurface AND :isNearMaxSurface " +
                "AND property_close_school = :isNearSchool " +
                "AND property_close_park = :isNearParc " +
                "AND property_close_shops = :isNearStore " +
                "AND property_photos >= :isNearNumberOfPhotos " +
                "AND property_price BETWEEN :isNearMinPrice AND :isNearMaxPrice " +
                "AND property_sale_status = :isNearSaleStatus "
    )
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
    @Query("SELECT * FROM property_table WHERE property_id= :id")
    fun getPropertyWithCursor(id: Int): Cursor

    @Query("SELECT * FROM property_table")
    fun getPropertyListWithCursor(): Cursor

    /**
     * Only for test purpose, app does not allow to delete properties
     */
    @Query("DELETE FROM property_table")
    fun nukeTable()
}