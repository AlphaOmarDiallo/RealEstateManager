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

    fun getNumberOfProperties() = if (!propertyList.value.isNullOrEmpty()) _propertyList.value?.size else 0

}