package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.model.media.InternalStoragePhoto
import com.example.realestatemanager.data.repository.dataStore.DataStoreRepository
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import com.example.realestatemanager.data.repository.property.PropertyRepository
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val mediaStoreRepository: MediaStoreRepository
) : ViewModel() {

    private var _pList = MutableLiveData<List<Property>>()
    val pList: LiveData<List<Property>> get() = _pList
    private var _propertyList = updateStateList()
    val propertyList: List<Property> get() = _propertyList
    val property: MutableState<Property> = mutableStateOf(SampleProperties.samplePropertyList[0])
    val currencyEuro: MutableState<Boolean> = mutableStateOf(false)
    val dollarToEuroRate: MutableState<Double> = mutableStateOf(Constant.DOLLARS_TO_EURO)

    private val _currencyEuro: MutableLiveData<Boolean> = MutableLiveData(false)
    val currencyEuroL: LiveData<Boolean> get() = _currencyEuro

    init {
        getPropertyList()
        checkCurrencyPreference()
        getDollarToEuroRate()
    }

    fun updateSelectedProperty(propertyToUpdate: Property) {
        viewModelScope.launch {
            property.value = propertyToUpdate
        }
    }

    @JvmName("getPropertyList1")
    fun getPropertyList() {
        viewModelScope.launch {
            _propertyList = propertyRepository.getListOfProperties().first().toMutableStateList()
            Log.e(TAG, "getPropertyList: _propertyList ${_propertyList.last().price}")
            Log.e(TAG, "getPropertyList: propertyList ${propertyList.last().price}")
            _pList.value = propertyRepository.getListOfProperties().first()
        }
    }

    private fun updateStateList(): List<Property> {
        var list: List<Property> = listOf()
        viewModelScope.launch {
            list = propertyRepository.getListOfProperties().first()
        }
        return list.toMutableStateList()
    }

    private fun checkCurrencyPreference() {
        viewModelScope.launch {
            currencyEuro.value = dataStoreRepository.readCurrencyPreference().first()
            _currencyEuro.value = dataStoreRepository.readCurrencyPreference().first()
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

    private val listInternalStoragePhoto: MutableState<List<InternalStoragePhoto>> =
        mutableStateOf(listOf())

    fun getListInternalPhoto(context: Context) {
        viewModelScope.launch {
            listInternalStoragePhoto.value =
                mediaStoreRepository.loadPhotosFromInternalStorage(context)
        }
    }

}