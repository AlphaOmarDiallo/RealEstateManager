package com.example.realestatemanager.data.repository.media

import android.content.Context
import android.graphics.Bitmap
import com.example.realestatemanager.data.model.InternalStoragePhoto

interface MediaStoreRepository {

    suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean

    suspend fun loadPhotosFromInternalStorage(context: Context): List<InternalStoragePhoto>

    suspend fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean

    fun getPhotoPath(context: Context, filename: String): String
}