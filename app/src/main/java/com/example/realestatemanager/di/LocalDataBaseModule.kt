package com.example.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.example.realestatemanager.data.localData.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            LocalDatabase::class.java,
            "LocalDatabase"
        )
            .build()

    @Singleton
    @Provides
    fun providePropertyDao(database: LocalDatabase) = database.propertyDao()

    @Singleton
    @Provides
    fun provideAgentDao(database: LocalDatabase) = database.agentDao()
}