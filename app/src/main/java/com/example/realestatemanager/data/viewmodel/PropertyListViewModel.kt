package com.example.realestatemanager.data.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
): ViewModel(){

    val propertyList: MutableState<List<Property>> = mutableStateOf(listOf())

    init {
        getPropertyList()
    }

    fun getPropertyList() {
        viewModelScope.launch{
            propertyList.value = propertyRepository.getListOfProperties().first()
        }
    }

}