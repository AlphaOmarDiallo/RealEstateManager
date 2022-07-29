package com.example.realestatemanager.data.repository.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveDollarToEuroRate(rate: Double)

    suspend fun saveEuroToDollarRate(rate: Double)

    fun readDollarToEuroRate(): Flow<Double>

    fun readEuroToDollarRate(): Flow<Double>

    /**
     * Testing only
     */
    fun getDataStore(): DataStore<Preferences>
}