package com.example.realestatemanager.data.repository.dataStore

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.realestatemanager.domain.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepositoryImp @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    private object PreferenceKeys {
        val euroToDollarRate = doublePreferencesKey("euroToDollarRate")
        val dollarToEuroRate = doublePreferencesKey("dollarToEuroRate")
    }

    override suspend fun saveDollarToEuroRate(rate: Double) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.dollarToEuroRate] = rate
        }
    }

    override suspend fun saveEuroToDollarRate(rate: Double) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.euroToDollarRate] = rate
        }
    }

    override fun readDollarToEuroRate(): Flow<Double> {
        val readFromDataStore: Flow<Double> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(TAG, "readDollarToEuroRate: DataStore ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val savedRate: Double =
                    preference[PreferenceKeys.dollarToEuroRate] ?: Constant.DOLLARS_TO_EURO
                savedRate
            }
        return readFromDataStore
    }

    override fun readEuroToDollarRate(): Flow<Double> {
        val readFromDataStore: Flow<Double> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(TAG, "readEuroToDollarRate: DataStore ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val savedRate: Double =
                    preference[PreferenceKeys.euroToDollarRate] ?: Constant.EURO_TO_DOLLARS
                savedRate
            }
        return readFromDataStore
    }

}