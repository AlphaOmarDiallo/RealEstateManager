package com.example.realestatemanager.data.viewmodel

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

    fun setFilteredList() {

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

        for (item in searchList) {

            if (type != null) {
                if (item.type != type) searchList.remove(item)
                break
            }

            if (city != null) {
                if (item.city != city) searchList.remove(item)
                break
            }

            if (neighbourhood != null) {
                if (item.neighbourhood != city) searchList.remove(item)
                break
            }

            if (school) {
                if (!item.closeToSchool) searchList.remove(item)
                break
            }

            if (shops) {
                if (!item.closeToShops) searchList.remove(item)
                break
            }

            if (parks) {
                if (!item.closeToPark) searchList.remove(item)
                break
            }

            if (soldLast3Month) {
                if (!item.closeToSchool) searchList.remove(item)
                break
            }

            if (addedLess7Days) {
                if (!item.closeToSchool) searchList.remove(item)
                break
            }

            val startPrice = startingPrice ?: 0
            val priceLimit = priceLimit ?: 10000000
            val sizeFrom = sizeFrom ?: 0
            val sizeTo = sizeUpTo ?: 100000

            if (item.price in startPrice..priceLimit) {
                println("")
            } else {
                searchList.remove(item)
                break
            }

            if (item.surface in sizeFrom..sizeTo) {
                println("")
            } else {
                searchList.remove(item)
            }

        }

        _filteredList.value = searchList

        return searchList.size
    }

}