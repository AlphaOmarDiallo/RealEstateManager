package com.example.realestatemanager.data.localData

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun latLngToString(latLng: LatLng) : String{
        return "(${latLng.latitude},${latLng.longitude}"
    }

    @TypeConverter
    fun stringToLatLng(string: String) : LatLng{
        val s = string.replace("(", "").replace(")", "")
        val list = s.split(",")
        return LatLng(list.first().toDouble(), list.last().toDouble())
    }

    @TypeConverter
    fun fromStringInt(value: String?): List<Int>? {
        val listType = object :
            TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListInt(list: List<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

}