package com.example.realestatemanager.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val agentRepository: AgentRepository
): ViewModel() {

    private val _currentUser: MutableLiveData<Agent>? = MutableLiveData()
    val currentUser: LiveData<Agent>? get() = _currentUser

    init {

    }

    /**
     * FireBase Repository
     */

    fun getCurrentUser() = if (firebaseAuthRepository.getCurrentUser() != null) firebaseAuthRepository.getCurrentUser() else null

    fun disconnectUser(context: Context) = firebaseAuthRepository.signOutCurrentUser(context)

    fun deleteAccountForever(context: Context) = firebaseAuthRepository.deleteCurrentUserForever(context)

    /**
     * Agent repository
     */

}