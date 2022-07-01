package com.example.realestatemanager.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.eurToUsd.EurToUsdRate

import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyAPIRepository: CurrencyAPIRepository
) : ViewModel() {

    val eurRate: MutableLiveData<EurToUsdRate> = MutableLiveData()

    fun getUSDtoEUR() {

    }

}