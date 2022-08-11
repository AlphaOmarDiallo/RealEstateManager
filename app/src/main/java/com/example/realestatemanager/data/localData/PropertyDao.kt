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

    //Todo add more queries to get filtered list in order to kee code clean

    /**
     * Content provider
     */
    @Query("SELECT * FROM property_table WHERE property_id= :id")
    fun getPropertyWithCursor(id: Int): Cursor

    /**
     * Only for test purpose, app does not allow to delete properties
     */
    @Query("DELETE FROM property_table")
    fun nukeTable()
}