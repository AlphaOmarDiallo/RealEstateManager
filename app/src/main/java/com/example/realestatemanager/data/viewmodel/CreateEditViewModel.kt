package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.nearBySearch.InternalStoragePhoto
import com.example.realestatemanager.data.model.nearBySearch.Result
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository,
    private val nearBySearchRepository: NearBySearchRepository
) : ViewModel() {

    private val _listInterest1: MutableList<Result> = mutableListOf()
    private val _listInterest: MutableLiveData<List<Result>> = MutableLiveData()
    val listInterest: LiveData<List<Result>> get() = _listInterest

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

    fun getPhotoPath(context: Context, filename: String) =
        mediaStoreRepository.getPhotoPath(context, filename)

    fun getInterestsAround(location: Location) {

        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, null)

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "getInterestsAround: no response from API", null)
                    return@launch
                }

                Log.e(TAG, "getInterestsAround: ${response.body()?.results}", )

                if (response.body()?.results != null) {
                    Log.i(ContentValues.TAG, "getInterestsAround: " + response.raw().request.url)
                    _listInterest.value = response.body()?.results
                } else {
                    Log.e(ContentValues.TAG, "getInterestsAround: null data", null)
                }
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "getInterestsAround: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "getInterestsAround: HttpException" + e.message(), null)

            }
        }
    }

    private fun getNextInterestAround(location: Location, token: String) {
        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, token)

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "getInterestsAround: no response from API", null)
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(ContentValues.TAG, "getInterestsAround: " + response.raw().request.url)
                    getNextInterestAround(location, response.body()!!.next_page_token)
                    _listInterest1.addAll(response.body()!!.results)
                    _listInterest.value = _listInterest1
                } else {
                    Log.e(ContentValues.TAG, "getInterestsAround: null data", null)
                }
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "getInterestsAround: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "getInterestsAround: HttpException" + e.message(), null)

            }
        }
    }

}