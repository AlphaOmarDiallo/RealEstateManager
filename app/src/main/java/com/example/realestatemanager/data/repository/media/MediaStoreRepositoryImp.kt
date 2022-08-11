package com.example.realestatemanager.data.repository.media

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import com.example.realestatemanager.data.model.media.InternalStoragePhoto
import com.example.realestatemanager.data.model.media.SharedStoragePhoto
import com.example.realestatemanager.domain.utils.sdk29AndUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject


class MediaStoreRepositoryImp @Inject constructor(): MediaStoreRepository {

    private lateinit var contentObserver: ContentObserver

    /**
     * INIT
     */
    override fun initContentObserver(context: Context) {
        contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                if (true) {
                    //loadPhotosFromInternalStorage(context)
                }
            }
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )
    }

    /**
     * Internal Storage
     */
    override suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean {
        return try {
            context.openFileOutput("$filename.webp", MODE_PRIVATE).use { stream ->
                if(!bmp.compress(Bitmap.CompressFormat.WEBP_LOSSY, 70, stream)) {
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
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".webp") }?.map {
                val bytes = it.readBytes()
                val uri = it.toURI()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp, uri)
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

    /**
     * External storage
     */

    override suspend fun loadPhotosFromExternalStorage(context: Context): List<SharedStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_ADDED
            )
            val photos = mutableListOf<SharedStoragePhoto>()
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

                while(cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    //Log.i(TAG, "loadPhotosFromExternalStorage: $contentUri")
                    photos.add(SharedStoragePhoto(id, displayName, width, height, contentUri))
                }
                photos.toList()
            } ?: listOf()
        }
    }

    override suspend fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }
            try {
                context.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    context.contentResolver.openOutputStream(uri).use { outputStream ->
                        if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                } ?: throw IOException("Couldn't create MediaStore entry")
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    /**
     *
     */

}