package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.media.InternalStoragePhoto
import com.example.realestatemanager.data.model.Photo
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.repository.dataStore.DataStoreRepository
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import com.example.realestatemanager.data.repository.photo.PhotoRepository
import com.example.realestatemanager.data.repository.property.PropertyRepository
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val mediaStoreRepository: MediaStoreRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    val location: MutableState<Location?> = mutableStateOf(Location("location"))
    val property: MutableState<Property> = mutableStateOf(SampleProperties.samplePropertyList[0])
    val currencyEuro: MutableState<Boolean> = mutableStateOf(false)
    val dollarToEuroRate: MutableState<Double> = mutableStateOf(Constant.DOLLARS_TO_EURO)

    init {
        checkCurrencyPreference()
        getDollarToEuroRate()
    }

    fun getProperty(id: Int) {
        viewModelScope.launch {
            property.value = propertyRepository.getProperty(id).first()
        }
    }

    /**
     * Currency repository
     */

    private fun checkCurrencyPreference() {
        viewModelScope.launch {
            currencyEuro.value = dataStoreRepository.readCurrencyPreference().first()
        }
    }

    private fun getDollarToEuroRate() {
        viewModelScope.launch {
            dollarToEuroRate.value = dataStoreRepository.readDollarToEuroRate().first()
        }
    }

    /**
     * MediaRepository
     */

    val listInternalStoragePhoto: MutableState<List<InternalStoragePhoto>> = mutableStateOf(listOf())
    val _listInternalStoragePhoto: MutableLiveData<List<InternalStoragePhoto>> = MutableLiveData()

    fun getListInternalPhoto(context: Context){
        viewModelScope.launch {
            _listInternalStoragePhoto.value = mediaStoreRepository.loadPhotosFromInternalStorage(context)
            Log.i(TAG, "getListInternalPhoto: in here")
        }
    }

    var propertyListImage: List<InternalStoragePhoto> = listOf()

    /**
     * Photo repository
     */

    fun getListOfPropertyPhoto(list: List<String>): List<Photo>{
        val listPhoto = mutableListOf<Photo>()

        viewModelScope.launch {
            /*for(item in list) {
                listPhoto.add(photoRepository.getPhotoWithName(item).first())
            }*/

            listPhoto.addAll(photoRepository.getListOfPhotos().first())
        }

        Log.e(TAG, "getListOfPropertyPhoto: $listPhoto", )
        
        return listPhoto
    }
}