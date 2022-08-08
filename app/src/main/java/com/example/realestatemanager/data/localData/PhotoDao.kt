package com.example.realestatemanager.data.localData

import androidx.room.*
import com.example.realestatemanager.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query ("SELECT * FROM photo_table")
    fun getListOfPhotos(): Flow<List<Photo>>

    @Query("SELECT * FROM photo_table WHERE photo_id= :id")
    fun getPhotoWithId(id: Int): Flow<Photo>

    @Query("SELECT * FROM photo_table WHERE photo_name= :name")
    fun getPhotoWithName(name: String): Flow<Photo>
}