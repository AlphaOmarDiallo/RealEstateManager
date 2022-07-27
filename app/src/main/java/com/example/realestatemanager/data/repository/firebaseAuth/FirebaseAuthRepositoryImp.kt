package com.example.realestatemanager.data.repository.firebaseAuth

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseAuthRepositoryImp @Inject constructor(): FirebaseAuthRepository {

    @Singleton
    @Provides
    fun provideFireBaseAuthRepository(): FirebaseAuthRepository =
        FirebaseAuthRepositoryImp()

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