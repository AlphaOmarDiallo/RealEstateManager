package com.example.realestatemanager.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(
    @ColumnInfo(name = "photo_bitmap")
    var bmp: Bitmap
) {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "photo_id")
    val id: Int = 0
}
