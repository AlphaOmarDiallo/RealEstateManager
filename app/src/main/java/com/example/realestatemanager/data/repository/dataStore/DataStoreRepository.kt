package com.example.realestatemanager.data.repository.dataStore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveDollarToEuroRate(rate: Double)

    suspend fun saveEuroToDollarRate(rate: Double)

    fun readDollarToEuroRate(): Flow<Double>

    fun readEuroToDollarRate(): Flow<Double>
}