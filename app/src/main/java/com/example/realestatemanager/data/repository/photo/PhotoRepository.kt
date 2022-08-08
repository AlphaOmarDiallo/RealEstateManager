package com.example.realestatemanager.data.repository.photo

import com.example.realestatemanager.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun insertPhoto(photo: Photo)

    suspend fun deletePhoto(photo: Photo)

    fun getListOfPhotos(): Flow<List<Photo>>

    fun getPhoto(id: Int): Flow<Photo>

    fun getPhotoWithName(name: String): Flow<Photo>

}