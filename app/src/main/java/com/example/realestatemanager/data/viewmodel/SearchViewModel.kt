package com.example.realestatemanager.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.repository.property.PropertyRepository
import com.example.realestatemanager.domain.utils.Utils
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
        sizeUpTo: Int?,
        numberOfPhoto: Boolean
    ) {
        searchQuery(
            buildTypeList(type),
            buildCityList(city),
            buildNeighbourhoodList(neighbourhood),
            buildMinSurface(sizeFrom),
            buildMaxSurface(sizeUpTo),
            buildSchoolList(school),
            buildShopsList(shops),
            buildParkList(parks),
            buildNumberOfPhoto(numberOfPhoto),
            buildMinPrice(startingPrice),
            buildMaxPrice(priceLimit),
            soldLast3Month,
            addedLess7Days
        )
    }

    private fun buildTypeList(type: String?): List<String> {
        val list: MutableList<String> = mutableListOf()

        if (type != null) {
            list.add(type)
        } else {
            val listP = propertyList.value
            for (item in listP!!) {
                if (!list.contains(item.type)) list.add(item.type)
            }
        }

        return list
    }

    private fun buildCityList(city: String?): List<String> {
        val list: MutableList<String> = mutableListOf()

        if (city != null) {
            list.add(city)
        } else {
            val listP = propertyList.value
            for (item in listP!!) {
                if (!list.contains(item.city)) list.add(item.city)
            }
        }

        return list
    }

    private fun buildNeighbourhoodList(neighbourhood: String?): List<String> {
        val list: MutableList<String> = mutableListOf()

        if (neighbourhood != null) {
            list.add(neighbourhood)
        } else {
            val listP = propertyList.value
            for (item in listP!!) {
                if (!list.contains(item.neighbourhood)) list.add(item.neighbourhood)
            }
        }

        return list
    }

    private fun buildMinSurface(sizeFrom: Int?) = sizeFrom ?: 0

    private fun buildMaxSurface(sizeUpTo: Int?) = sizeUpTo ?: 1000000

    private fun buildMinPrice(startingPrice: Int?) = startingPrice ?: 0

    private fun buildMaxPrice(maxPrice: Int?) = maxPrice ?: 100000000

    private fun buildSchoolList(school: Boolean): List<Boolean> {
        val list: MutableList<Boolean> = mutableListOf()

        if (school) {
            list.add(school)
        } else {
            list.add(true)
            list.add(false)
        }

        return list
    }

    private fun buildShopsList(school: Boolean): List<Boolean> {
        val list: MutableList<Boolean> = mutableListOf()

        if (school) {
            list.add(school)
        } else {
            list.add(true)
            list.add(false)
        }

        return list
    }

    private fun buildParkList(school: Boolean): List<Boolean> {
        val list: MutableList<Boolean> = mutableListOf()

        if (school) {
            list.add(school)
        } else {
            list.add(true)
            list.add(false)
        }

        return list
    }

    private fun buildNumberOfPhoto(numberOfPhoto: Boolean) = if (numberOfPhoto) 3 else 0


    private fun searchQuery(
        isNearTypeProperty: List<String>,
        isNearCity: List<String>,
        isNearNeighbourhood: List<String>,
        isNearMinSurface: Int,
        isNearMaxSurface: Int,
        isNearSchool: List<Boolean>,
        isNearStore: List<Boolean>,
        isNearParc: List<Boolean>,
        isNearNumberOfPhotos: Int,
        isNearMinPrice: Int,
        isNearMaxPrice: Int,
        isNearSaleStatus: Boolean,
        isAddedLastSevenDays: Boolean
    ) {
        val temp: MutableList<Property> = mutableListOf()

        viewModelScope.launch {
            temp.addAll(
                propertyRepository.getPropertyResearch(
                    isNearTypeProperty,
                    isNearCity,
                    isNearNeighbourhood,
                    isNearMinSurface,
                    isNearMaxSurface,
                    isNearSchool,
                    isNearStore,
                    isNearParc,
                    isNearNumberOfPhotos,
                    isNearMinPrice,
                    isNearMaxPrice,
                    isNearSaleStatus,
                ).first()
            )

            if (temp.isNotEmpty()){

                if (isNearSaleStatus) {
                    for (item in temp){
                        if (item.offTheMarketSince!! < Utils.dateMinusThreeMonth()) {
                            temp.remove(item)
                        }
                    }
                }

                if (isAddedLastSevenDays){
                    for (item in temp){
                        if (item.onTheMarketSince!! > Utils.dateMinusSevenDays()) {
                            temp.remove(item)
                        }
                    }
                }
            }

            _filteredList.value = temp
        }
    }

}