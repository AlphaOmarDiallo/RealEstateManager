package com.example.realestatemanager.data.model.media

import android.graphics.Bitmap
import java.net.URI

data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap,
    val uri: URI
)
