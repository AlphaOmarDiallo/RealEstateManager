package com.example.realestatemanager.testDI

import android.content.Context
import androidx.room.Room
import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.di.LocalDataBaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDataBaseModule::class]
)
class TestLocalDataBaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            LocalDatabase::class.java,
            "TestLocalDatabase"
        ).build()

    @Singleton
    @Provides
    fun providePropertyDao(database: LocalDatabase) = database.propertyDao()

    @Singleton
    @Provides
    fun provideAgentDao(database: LocalDatabase) = database.agentDao()

    @Singleton
    @Provides
    fun providePhotoDao(database: LocalDatabase) = database.photoDao()
}