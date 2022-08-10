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
import com.example.realestatemanager.data.localData.PhotoDao
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Photo
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.model.media.InternalStoragePhoto
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
    private val agentRepository: AgentRepository,
    private val photoDao: PhotoDao
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

    private val _listInternalPhoto: MutableLiveData<List<InternalStoragePhoto>> = MutableLiveData()
    val listInternalPhoto: LiveData<List<InternalStoragePhoto>> get() = _listInternalPhoto

    private val _listExternalPhoto: MutableLiveData<List<SharedStoragePhoto>> = MutableLiveData()
    val listExternalPhoto: LiveData<List<SharedStoragePhoto>> get() = _listExternalPhoto

    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, context: Context): Boolean {
        var result = false
        viewModelScope.launch {
            result = mediaStoreRepository.savePhotoToInternalStorage(filename, bmp, context)
        }
        return result
    }

    fun loadPhotosFromInternalStorage(context: Context) {
        viewModelScope.launch {
            _listInternalPhoto.value = mediaStoreRepository.loadPhotosFromInternalStorage(context)
        }
    }

    fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean {
        var result = false
        viewModelScope.launch {
            result = mediaStoreRepository.deletePhotoFromInternalStorage(filename, context)
        }
        return result
    }

    fun getPhotoPath(context: Context, filename: String) =
        mediaStoreRepository.getPhotoPath(context, filename)


    fun initContentProvider(context: Context) = mediaStoreRepository.initContentObserver(context)

    fun loadPhotoFromExternalStorage(context: Context) = viewModelScope.launch { _listExternalPhoto.value = mediaStoreRepository.loadPhotosFromExternalStorage(context) }

    fun savePhotoInExternalStorage(name: String, bmp: Bitmap, context: Context): Boolean {
        var result = false
        viewModelScope.launch { result = mediaStoreRepository.savePhotoToExternalStorage(name, bmp, context) }
        return result
    }

    /**
     * Photo Dao
     */

    private val _listPhotoRoom: MutableLiveData<List<Photo>> = MutableLiveData()
    val listPhotoRoom: LiveData<List<Photo>> get() = _listPhotoRoom

    fun getPhotoList() {
        viewModelScope.launch {
            _listPhotoRoom.value = photoDao.getListOfPhotos().first()
        }
    }

    fun savePhotoInRoom(internalStoragePhoto: InternalStoragePhoto) {
        var isAlreadyInDB = false
        var photo = Photo(0, internalStoragePhoto.bmp, internalStoragePhoto.name)
        for (item in listPhotoRoom.value!!) {
            if (item.name == photo.name) {
                isAlreadyInDB = true
                break
            }
        }
        viewModelScope.launch {
            if (!isAlreadyInDB) {
                photoDao.insertPhoto(photo)
            }

        }
    }

    fun getPhotoByName(name: String): Photo? {
        var photo: Photo? = null
        viewModelScope.launch {
            photo = photoDao.getPhotoWithName(name).first()
        }
        return photo
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
            if (agentRepository.getAllAgent().first() != null) _listAgent.value =
                agentRepository.getAllAgent().first()
        }
    }

    fun getAgentID(name: String): String {
        var id: String = ""
        for (agent in listAgent.value!!) {
            if (agent.name.equals(name)) {
                id = agent.id
                break
            }
        }
        return id
    }

}