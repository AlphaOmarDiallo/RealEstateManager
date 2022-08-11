package com.example.realestatemanager.data.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.model.media.SharedStoragePhoto
import com.example.realestatemanager.data.model.nearBySearch.Result
import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import com.example.realestatemanager.data.repository.nearBySearch.NearBySearchRepository
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository,
    private val nearBySearchRepository: NearBySearchRepository,
    private val propertyRepository: PropertyRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _listInterest1: MutableList<Result> = mutableListOf()
    private val _listInterest: MutableLiveData<List<Result>> = MutableLiveData()
    val listInterest: LiveData<List<Result>> get() = _listInterest

    init {
        getAgentList()
    }

    /**
     * Media Storage repository
     */

    private val _listExternalPhoto: MutableLiveData<List<SharedStoragePhoto>> = MutableLiveData()
    val listExternalPhoto: LiveData<List<SharedStoragePhoto>> get() = _listExternalPhoto

    fun initContentProvider(context: Context) = mediaStoreRepository.initContentObserver(context)

    fun loadPhotoFromExternalStorage(context: Context) = viewModelScope.launch {
        _listExternalPhoto.value = mediaStoreRepository.loadPhotosFromExternalStorage(context)
    }

    fun savePhotoInExternalStorage(name: String, bmp: Bitmap, context: Context): Boolean {
        var result = true
        viewModelScope.launch {
            result = mediaStoreRepository.savePhotoToExternalStorage(name, bmp, context)
        }
        return result
    }

    /**
     * NearBySearch repository
     */

    fun getInterestsAround(location: Location) {

        viewModelScope.launch {
            try {
                val response = nearBySearchRepository.getInterestList(location, null)

                if (!response.isSuccessful) {
                    Log.w(ContentValues.TAG, "getInterestsAround: no response from API", null)
                    return@launch
                }

                Log.e(ContentValues.TAG, "getInterestsAround: ${response.body()?.results}")

                if (response.body()?.results != null) {
                    Log.i(
                        ContentValues.TAG,
                        "getInterestsAround: " + response.raw().request.url
                    )
                    _listInterest1.addAll(response.body()!!.results)
                    _listInterest.value = _listInterest1
                } else {
                    Log.e(ContentValues.TAG, "getInterestsAround: null data", null)
                }
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, "getInterestsAround: IOException" + e.message, null)

            } catch (e: HttpException) {
                Log.e(
                    ContentValues.TAG,
                    "getInterestsAround: HttpException" + e.message(),
                    null
                )
            }
        }
    }

    /**
     * Property repository
     */

    fun insertProperty(property: Property) {
        viewModelScope.launch { propertyRepository.insertProperty(property) }
    }

    fun updateProperty(property: Property) {
        viewModelScope.launch { propertyRepository.updateProperty(property) }
    }

    /**
     * Agent repository
     */

    private val _listAgent: MutableLiveData<List<Agent>?> = MutableLiveData()
    val listAgent: LiveData<List<Agent>?> get() = _listAgent

    fun getAgent(agentID: String) = agentRepository.getAgentById(agentID)

    fun getAgentList() {
        viewModelScope.launch {
            if (_listAgent != null) _listAgent.value =
                agentRepository.getAllAgent().first()
        }
    }

    fun getAgentID(name: String): String {
        var id = ""
        for (agent in listAgent.value!!) {
            if (agent.name == name) {
                id = agent.id
                break
            }
        }
        return id
    }

}