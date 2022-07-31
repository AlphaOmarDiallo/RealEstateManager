package com.example.realestatemanager.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.dataStore.DataStoreRepository
import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val agentRepository: AgentRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _currentUser: MutableLiveData<Agent> = MutableLiveData()
    val currentUser: LiveData<Agent> get() = _currentUser

    init {
        readSavedAgentIDFromDataStore()
    }

    fun createAgent(agent: Agent) {
        insertAgentToDatabase(agent)
        saveAgentToDataStore(agent.id)
        setAgent(agent)
    }

    fun deleteAgent(context: Context) {
        deleteAccountForever(context)
        currentUser.value?.let { deleteAgentInDatabase(it) }
    }

    private fun setAgent(agent: Agent) {
        _currentUser.value = agent
    }

    /**
     * FireBase Repository
     */

    fun getCurrentUser() =
        if (firebaseAuthRepository.getCurrentUser() != null) firebaseAuthRepository.getCurrentUser() else null

    fun disconnectUser(context: Context) = firebaseAuthRepository.signOutCurrentUser(context)

    private fun deleteAccountForever(context: Context) =
        firebaseAuthRepository.deleteCurrentUserForever(context)

    /**
     * Agent repository
     */

    fun insertAgentToDatabase(agent: Agent) {
        viewModelScope.launch {
            agentRepository.insertAgent(agent)
        }
    }

    fun deleteAgentInDatabase(agent: Agent) {
        viewModelScope.launch {
            agentRepository.deleteAgent(agent)
        }
    }

    /**
     * DataStore repository
     */

    private fun readSavedAgentIDFromDataStore() {
        viewModelScope.launch {
            val agentID = dataStoreRepository.readAgentID().first()
            if (agentID != "") {
                val agent = agentRepository.getAgentById(agentID).first()
                _currentUser.value = agent
            }
        }
    }

    private fun saveAgentToDataStore(id: String) {
        viewModelScope.launch { dataStoreRepository.saveAgentID(id) }
    }

    fun readCurrencyPreferenceFromDataStore(): Boolean {
        var pref = false
        viewModelScope.launch {
            pref = dataStoreRepository.readCurrencyPreference().first()
        }
        return pref
    }

    fun saveCurrencyPreferenceToDataStore(preference: Boolean) {
        viewModelScope.launch { dataStoreRepository.saveCurrencyPreference(preference) }
    }

    fun readNotificationPreferenceFromDataStore(): Boolean {
        var pref = false
        viewModelScope.launch {
            pref = dataStoreRepository.readNotificationPreference().first()
        }
        return pref
    }

    fun saveNotificationPreferenceToDataStore(preference: Boolean) {
        viewModelScope.launch { dataStoreRepository.saveNotificationPreference(preference) }
    }

}