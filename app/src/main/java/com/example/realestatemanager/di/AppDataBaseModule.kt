package com.example.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.example.realestatemanager.data.localData.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()

    @Singleton
    @Provides
    fun providePropertyDao(database: AppDatabase) = database.propertyDao()

    @Singleton
    @Provides
    fun provideAgentDao(database: AppDatabase) = database.agentDao()
}