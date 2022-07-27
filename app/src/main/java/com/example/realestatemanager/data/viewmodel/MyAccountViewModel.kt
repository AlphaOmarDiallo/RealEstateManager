package com.example.realestatemanager.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {

    fun getCurrentUser(): FirebaseUser?{
        return if (firebaseAuthRepository.getCurrentUser() != null) firebaseAuthRepository.getCurrentUser() else null
    }

    fun disconnectUser(context: Context) = firebaseAuthRepository.signOutCurrentUser(context)
}