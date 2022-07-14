package com.example.realestatemanager.data.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.repositories.connectivity.ConnectivityRepository
import com.example.realestatemanager.data.repositories.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.data.repositories.geocoding.GeocodingRepository
import com.example.realestatemanager.data.repositories.nearBySearch.NearBySearchRepository
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
    private val nearBySearchRepository: NearBySearchRepository
) : ViewModel() {

    val usdRate: MutableLiveData<Double> = MutableLiveData()
    val eurRate: MutableLiveData<Double> = MutableLiveData()

    @JvmName("getUsdRate1")
    fun getUsdRate() {
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertEURtoUSD()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getUsdRate: no response from API", null)
                    usdRate.value = Constant.EURO_TO_DOLLARS
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(TAG, "exchange rate " + response.body()?.exchange_rates)
                    usdRate.value = response.body()?.exchange_rates!!.USD
                } else {
                    Log.e(TAG, "getUsdRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "getUsdRate: IOException" + e.message, null)
                usdRate.value = Constant.EURO_TO_DOLLARS
            } catch (e: HttpException) {
                Log.e(TAG, "getUsdRate: HttpException" + e.message(), null)
                usdRate.value = Constant.EURO_TO_DOLLARS
            }
        }
    }

    fun getEurRate() {
        viewModelScope.launch {
            try {
                val response = currencyAPIRepository.convertUSDtoEUR()

                if (!response.isSuccessful) {
                    Log.w(TAG, "getEurRate: no response from API", null)
                    eurRate.value = Constant.DOLLARS_TO_EURO
                    return@launch
                }

                if (response.body()?.exchange_rates != null) {
                    Log.i(TAG, "exchange rate " + response.body()?.exchange_rates)
                    eurRate.value = response.body()?.exchange_rates!!.EUR
                } else {
                    Log.e(TAG, "getEurRate: null data", null)
                }

            } catch (e: IOException) {
                Log.e(TAG, "getEurRate: IOException" + e.message, null)
                eurRate.value = Constant.DOLLARS_TO_EURO
            } catch (e: HttpException) {
                Log.e(TAG, "getEurRate: HttpException" + e.message(), null)
                eurRate.value = Constant.DOLLARS_TO_EURO
            }
        }
    }

    fun checkConnectivity(context: Context): LiveData<Boolean> {
        return connectivityRepository.isInternetAvailable(context)
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
                    Log.w(TAG, "getInterestsAround: no response from API", null )
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(TAG, "getInterestsAround: " + response.raw().request.url)
                    if (response.body()!!.next_page_token != null) {
                        getNextInterestAround(location, response.body()!!.next_page_token)
                    }
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

    private fun getNextInterestAround(location: Location, token: String){
        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, token)

                if (!response.isSuccessful) {
                    Log.w(TAG, "getInterestsAround: no response from API", null )
                    return@launch
                }

                if (response.body()?.results != null) {
                    Log.i(TAG, "getInterestsAround: " + response.raw().request.url)
                    if (response.body()!!.next_page_token != null) {
                            getNextInterestAround(location, response.body()!!.next_page_token)
                    }
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
}
