package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.autocomplete.AutocompleteRepository
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repository.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repository.dataStore.DataStoreRepository
import com.example.realestatemanager.data.repository.geocoding.GeocodingRepository
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepository
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyAPIRepository: CurrencyAPIRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val geocodingRepository: GeocodingRepository,
    private val nearBySearchRepository: NearBySearchRepository,
    private val autocompleteRepository: AutocompleteRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _usdRate: MutableLiveData<Double> = MutableLiveData()
    val usdRate: LiveData<Double> get() = _usdRate

    private val _eurRate: MutableLiveData<Double> = MutableLiveData()
    val eurRate: LiveData<Double> get() = _eurRate

    private val _connected: MutableLiveData<Boolean> = MutableLiveData()
    val connected: LiveData<Boolean> get() = _connected

    init {
        getEurRate()
        getUsdRate()
    }

    /**
     * DataStore Repository
     */

    fun saveEuroToDollarRateToDataStore(rate: Double) =
        viewModelScope.launch {
            dataStoreRepository.saveEuroToDollarRate(rate)
        }

    fun saveDollarToEuroRateToDataStore(rate: Double) =
        viewModelScope.launch {
            dataStoreRepository.saveDollarToEuroRate(rate)
        }

    /**
     * Currency Repository
     */
    @JvmName("getUsdRate1")
    fun getUsdRate() {
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertEURtoUSD()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getUsdRate: no response from API", null)
                    _usdRate.value = Constant.EURO_TO_DOLLARS
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(TAG, "exchange rate " + response.body()?.exchange_rates)
                    _usdRate.value = response.body()?.exchange_rates!!.USD
                } else {
                    Log.e(TAG, "getUsdRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "getUsdRate: IOException" + e.message, null)
                _usdRate.value = Constant.EURO_TO_DOLLARS
            } catch (e: HttpException) {
                Log.e(TAG, "getUsdRate: HttpException" + e.message(), null)
                _usdRate.value = Constant.EURO_TO_DOLLARS
            }
        }
    }

    private fun getEurRate() {
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertUSDtoEUR()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getEurRate: no response from API", null)
                    _eurRate.value = Constant.DOLLARS_TO_EURO
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(TAG, "exchange rate " + response.body()?.exchange_rates)
                    _eurRate.value = response.body()?.exchange_rates!!.EUR
                } else {
                    Log.e(TAG, "getEurRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "getEurRate: IOException" + e.message, null)
                _eurRate.value = Constant.DOLLARS_TO_EURO
            } catch (e: HttpException) {
                Log.e(TAG, "getEurRate: HttpException" + e.message(), null)
                _eurRate.value = Constant.DOLLARS_TO_EURO
            }
        }
    }

    fun checkConnectivity(context: Context) {
        viewModelScope.launch {
            _connected.value = connectivityRepository.isInternetAvailable(context).value
        }
    }

    fun convertAddressToGeocode() {
        viewModelScope.launch {
            try {
                val response =
                    geocodingRepository.convertAddressToGeocode("strada traian vuia 3  otopeni")

                if (!response.isSuccessful) {
                    Log.w(TAG, "geocode: no response from API", null)
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(TAG, "geocode " + response.body()?.results)
                    Log.i(TAG, "geocoding " + response.body()?.results!![0].geometry.location)
                } else {
                    Log.e(TAG, "geocode: null data", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "geocode: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(TAG, "geocode: HttpException" + e.message(), null)

            }
        }
    }

    fun getInterestsAround() {
        val location = Location("location")
        location.latitude = 48.877767960424706
        location.longitude = 2.3385472865091392

        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, null)

                if (!response.isSuccessful) {
                    Log.w(TAG, "getInterestsAround: no response from API", null)
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(TAG, "getInterestsAround: " + response.raw().request.url)
                    getNextInterestAround(location, response.body()!!.next_page_token)
                } else {
                    Log.e(TAG, "getInterestsAround: null data", null)
                }
            } catch (e: IOException) {
                Log.e(TAG, "getInterestsAround: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(TAG, "getInterestsAround: HttpException" + e.message(), null)

            }
        }
    }

    private fun getNextInterestAround(location: Location, token: String) {
        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, token)

                if (!response.isSuccessful) {
                    Log.w(TAG, "getInterestsAround: no response from API", null)
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(TAG, "getInterestsAround: " + response.raw().request.url)
                    getNextInterestAround(location, response.body()!!.next_page_token)
                } else {
                    Log.e(TAG, "getInterestsAround: null data", null)
                }
            } catch (e: IOException) {
                Log.e(TAG, "getInterestsAround: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(TAG, "getInterestsAround: HttpException" + e.message(), null)

            }
        }
    }

    fun getAutocompleteSuggestions(input: String) {
        viewModelScope.launch {
            try {
                val response = autocompleteRepository.autocompleteAddress(input)

                if (!response.isSuccessful) {
                    Log.w(TAG, "getAutocompleteSuggestions: no response from API", null)
                    return@launch
                }

                if (response.body()?.predictions != null) {
                    Log.i(TAG, "getAutocompleteSuggestions: " + response.raw().request.url)
                } else {
                    Log.e(TAG, "getAutocompleteSuggestions", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "getAutocompleteSuggestions: IOException" + e.message, null)
            } catch (e: HttpException) {
                Log.e(TAG, "getAutocompleteSuggestions: HttpException" + e.message(), null)
            }
        }
    }
}
