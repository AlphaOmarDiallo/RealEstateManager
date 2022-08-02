package com.example.realestatemanager.data.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.InternalStoragePhoto
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
): ViewModel() {

    init {

    }

    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean {
        var result = false
        viewModelScope.launch {
            result = mediaStoreRepository.savePhotoToInternalStorage(filename, bmp, context)
        }
        return result
    }

    fun loadPhotosFromInternalStorage(context: Context): List<InternalStoragePhoto> {
        var photoList: List<InternalStoragePhoto> = listOf()
        viewModelScope.launch {
            photoList = mediaStoreRepository.loadPhotosFromInternalStorage(context)
        }
        return photoList
    }

    fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean {
        var result = false
        viewModelScope.launch {
            result = mediaStoreRepository.deletePhotoFromInternalStorage(filename, context)
        }
        return result
    }

    fun getPhotoPath(context: Context, filename: String) = mediaStoreRepository.getPhotoPath(context, filename)
}