package com.example.realestatemanager.data.repository.firebaseAuth

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseAuthRepositoryImp @Inject constructor() : FirebaseAuthRepository {

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getCurrentUserID(): String? {
        val user = getCurrentUser()
        return user?.uid
    }

    override fun signOutCurrentUser(context: Context) {
        AuthUI.getInstance().signOut(context)
    }

    override fun deleteCurrentUserForever(context: Context) {
        AuthUI.getInstance().delete(context)
    }
}