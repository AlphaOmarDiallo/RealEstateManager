package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _propertyList: MutableLiveData<List<Property>> = MutableLiveData()
    val propertyList: LiveData<List<Property>> get() = _propertyList

    private val _filteredList: MutableLiveData<List<Property>> = MutableLiveData()
    val filteredList: LiveData<List<Property>> get() = _filteredList

    fun getPropertyList() {
        viewModelScope.launch {
            _propertyList.value = propertyRepository.getListOfProperties().first()
        }
    }

    fun getNumberOfProperties() =
        if (!propertyList.value.isNullOrEmpty()) _propertyList.value?.size else 0

    fun searchUserCriteria(
        type: String?,
        city: String?,
        neighbourhood: String?,
        school: Boolean,
        shops: Boolean,
        parks: Boolean,
        soldLast3Month: Boolean,
        addedLess7Days: Boolean,
        startingPrice: Int?,
        priceLimit: Int?,
        sizeFrom: Int?,
        sizeUpTo: Int?
    ): Int {
        val searchList: MutableList<Property> = mutableListOf()
        searchList.addAll(propertyList.value!!)

        val toKeepList: MutableList<Property> = mutableListOf()

        if(type != null){
            for (item in searchList){
                if (item.type == type){
                    toKeepList.add(item)
                }
            }
        }

        if(city != null){
            for (item in searchList){
                if (item.city == city){
                    if (searchList.contains(item)) Log.i(TAG, "searchUserCriteria: already in list") else toKeepList.add(item)
                }
            }
        }

        if(neighbourhood != null){
            for (item in searchList){
                if (item.neighbourhood == neighbourhood){
                    if (searchList.contains(item)) Log.i(TAG, "searchUserCriteria: already in list") else toKeepList.add(item)
                }
            }
        }

        for (item in searchList){
            if(item.closeToSchool == school) if (searchList.contains(item)) Log.i(TAG, "searchUserCriteria: already in list") else toKeepList.add(item)
        }

        for (item in searchList){
            if(item.closeToShops == shops) if (searchList.contains(item)) Log.i(TAG, "searchUserCriteria: already in list") else toKeepList.add(item)
        }

        for (item in searchList){
            if(item.closeToPark == parks) if (searchList.contains(item)) Log.i(TAG, "searchUserCriteria: already in list") else toKeepList.add(item)
        }

        /*for (item in searchList) {

            var toKeep = false

            if (type != null) {
                if (item.type == type.toString()) toKeep = true
            }

            if (city != null) {
                if (item.city == city.toString()) toKeep = true
            }

            if (neighbourhood != null) {
                if (item.neighbourhood == neighbourhood.toString()) toKeep = true
            }

            if (school) {
                if (item.closeToSchool) toKeep = true
            }

            if (shops) {
                if (item.closeToShops) toKeep = true
            }

            if (parks) {
                if (item.closeToPark) toKeep = true
            }

            if (soldLast3Month) {
                if (item.closeToSchool) toKeep = true
            }

            if (addedLess7Days) {
                if (item.closeToSchool) toKeep = true
            }

            val startPrice = startingPrice ?: 0
            val priceLim = if (priceLimit == 0 || priceLimit == null) 10000000 else priceLimit
            val sizeFr = sizeFrom ?: 0
            val sizeTo = if (sizeUpTo == 0 || sizeUpTo == null) 10000000 else sizeUpTo

            if (item.price in startPrice..priceLim) toKeep = true

            if (item.surface in sizeFr..sizeTo) toKeep = true

            if (toKeep) toKeepList.add(item)*/
        /*}


        _filteredList.value = toKeepList

        Log.e("test", "searchUserCriteria: ${searchList.size}")*/

        Log.e("test", "searchUserCriteria: ${toKeepList.size}")
        return searchList.size
    }

}