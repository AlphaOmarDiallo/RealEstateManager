package com.example.realestatemanager.data.repository.dataStore

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
        val agentID = stringPreferencesKey("agentID")
        val currency = booleanPreferencesKey("currencyPreferenceSwitch")
        val notification = booleanPreferencesKey("notificationPreferences")
    }

    /**
     * Currency
     */

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

    /**
     * Agent
     */

    override suspend fun saveAgentID(agentID: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.agentID] = agentID
        }
    }

    override fun readAgentID(): Flow<String> {
        val readFromDataStore: Flow<String> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(TAG, "readAgentID: DataStore ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val id: String =
                    preference[PreferenceKeys.agentID] ?: ""
                id
            }
        return readFromDataStore
    }

    /**
     * User preferences
     */

    override suspend fun saveCurrencyPreference(currency: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.currency] = currency
        }
    }

    override fun readCurrencyPreference(): Flow<Boolean> {
        val readFromDataStore: Flow<Boolean> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(TAG, "readCurrencyPreference: DataStore ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val currency: Boolean =
                    preference[PreferenceKeys.currency] ?: false
                currency
            }
        return readFromDataStore
    }

    override suspend fun saveNotificationPreference(notification: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.notification] = notification
        }
    }

    override fun readNotificationPreference(): Flow<Boolean> {
        val readFromDataStore: Flow<Boolean> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(TAG, "readNotificationPreference: DataStore ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val notification: Boolean =
                    preference[PreferenceKeys.notification] ?: false
                notification
            }
        return readFromDataStore
    }

    /**
     * Testing only
     */

    override fun getDataStore(): DataStore<Preferences> = dataStore

}