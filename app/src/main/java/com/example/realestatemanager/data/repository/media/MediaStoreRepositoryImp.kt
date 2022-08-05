package com.example.realestatemanager.data.repository.media

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.realestatemanager.data.model.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject


class MediaStoreRepositoryImp @Inject constructor(): MediaStoreRepository {

    override suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean {
        return try {
            context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun loadPhotosFromInternalStorage(context: Context): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

    override fun getPhotoPath(context: Context, filename: String): String {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir("dirName", MODE_PRIVATE)
        val myPath = File(directory, filename)
        return myPath.toString()
    }

    override suspend fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean {
        return try {
            context.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}