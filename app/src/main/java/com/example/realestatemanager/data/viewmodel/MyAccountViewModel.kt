package com.example.realestatemanager.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _currentUser: MutableLiveData<Agent>? = MutableLiveData()
    val currentUser: LiveData<Agent>? get() = _currentUser

    init {

    }

    fun setAgent(agent: Agent) {
        _currentUser!!.value = agent
    }

    fun createAgent(agent: FirebaseUser){
        val agent = Agent(
            agent.uid,
            agent.displayName.toString(),
            agent.email.toString(),
            agent?.photoUrl.toString()
        )

        insertAgentToDatabase(agent)
    }


    /**
     * FireBase Repository
     */

    fun getCurrentUser() =
        if (firebaseAuthRepository.getCurrentUser() != null) firebaseAuthRepository.getCurrentUser() else null

    fun disconnectUser(context: Context) = firebaseAuthRepository.signOutCurrentUser(context)

    fun deleteAccountForever(context: Context) =
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

    fun getAgentByIdInDatabase(agentID: String) = agentRepository.getUserById(agentID)

}