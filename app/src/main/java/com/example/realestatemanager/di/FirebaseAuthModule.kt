package com.example.realestatemanager.di

import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepository
import com.example.realestatemanager.data.repository.firebaseAuth.FirebaseAuthRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseAuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthRepository(): FirebaseAuthRepository =
        FirebaseAuthRepositoryImp()
}