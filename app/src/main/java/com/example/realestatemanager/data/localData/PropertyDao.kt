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