package com.example.realestatemanager.data.repository.media

import android.content.Context
import android.graphics.Bitmap
import com.example.realestatemanager.data.model.media.InternalStoragePhoto
import com.example.realestatemanager.data.model.media.SharedStoragePhoto

interface MediaStoreRepository {

    suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean

    suspend fun loadPhotosFromInternalStorage(context: Context): List<InternalStoragePhoto>

    suspend fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean

    fun getPhotoPath(context: Context, filename: String): String

    suspend fun loadPhotosFromExternalStorage(context: Context): List<SharedStoragePhoto>

    fun initContentObserver(context: Context)

    suspend fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap, context: Context): Boolean
}