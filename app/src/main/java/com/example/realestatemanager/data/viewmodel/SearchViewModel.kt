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

    private val _PropertyList: MutableLiveData<List<Property>> = MutableLiveData()
    val propertyList: LiveData<List<Property>> get() = _PropertyList

    init {
        getPropertyList()
    }

    private fun getPropertyList() {
        viewModelScope.launch {
            _PropertyList.value = propertyRepository.getListOfProperties().first()
        }
    }

}