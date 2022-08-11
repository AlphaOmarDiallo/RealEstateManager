package com.example.realestatemanager.domain.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.RoomMasterTable.TABLE_NAME
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.EntryPoints
import javax.inject.Inject

class PropertyContentProvider @Inject constructor() : ContentProvider() {

    lateinit var propertyRepository: PropertyRepository

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    private val property = 1
    private val propertyID = 2

    init {
        sURIMatcher.addURI(AUTHORITY, PROPERTY_TABLE, property)
        sURIMatcher.addURI(AUTHORITY, "$PROPERTY_TABLE/#",propertyID)
    }

    override fun onCreate(): Boolean {
        propertyRepository = EntryPoints.get(context!!, ContentProviderUtilities::class.java).propertyRepository
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor {
        return if (context != null) {
            val cursor = propertyRepository.getPropertyListWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Failed to query row for uri $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }

    companion object{
        const val AUTHORITY = "com.example.realestatemanager.provider"
        private const val PROPERTY_TABLE = "property_table"
        val CONTENT_URI : Uri = Uri.parse("content://$AUTHORITY/$PROPERTY_TABLE")
    }
}