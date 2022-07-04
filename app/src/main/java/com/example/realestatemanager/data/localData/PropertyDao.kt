package com.example.realestatemanager.data.localData

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

    //Todo add more queries to get filtered list in order to kee code clean
}