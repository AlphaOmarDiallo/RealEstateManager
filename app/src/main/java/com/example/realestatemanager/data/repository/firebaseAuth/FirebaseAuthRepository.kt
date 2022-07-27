package com.example.realestatemanager.data.repository.firebaseAuth

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthRepository {

    fun getCurrentUser(): FirebaseUser?

    fun getCurrentUserID(): String?

    fun signOutCurrentUser(context: Context)

    fun deleteCurrentUserForever(context: Context)

}