package com.example.realestatemanager.data.repository.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.realestatemanager.data.sampleData.SampleAgent
import com.example.realestatemanager.domain.Constant
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class DataStoreRepositoryImpTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        hiltRule.inject()
        dataStore = dataStoreRepository.getDataStore()
    }

    @After
    fun tearDown() {
        runBlocking { dataStore.edit { it.clear() } }
    }

    @Test
    fun dataStoreRepository_is_not_null() = assertThat(dataStoreRepository).isNotNull()

    @Test
    fun check_that_saving_euro_to_dollar_rate_is_working_as_expected() = runTest {
        //Given
        val expectedRate = Constant.EURO_TO_DOLLARS
        dataStoreRepository.saveEuroToDollarRate(Constant.EURO_TO_DOLLARS)
        advanceUntilIdle()

        //When
        val savedRate = dataStoreRepository.readEuroToDollarRate().first()
        advanceUntilIdle()

        assertThat(savedRate).isEqualTo(expectedRate)

    }

    @Test
    fun check_that_saved_dollar_to_euro_rate_is_correct() = runTest{
        //Given
        val expectedRate = Constant.DOLLARS_TO_EURO
        dataStoreRepository.saveDollarToEuroRate(Constant.DOLLARS_TO_EURO)
        advanceUntilIdle()

        //When
        val savedRate = dataStoreRepository.readDollarToEuroRate().first()

        //Then
        assertThat(savedRate).isEqualTo(expectedRate)
    }

    @Test
    fun check_that_reading_euro_to_dollar_rate_is_correct() = runTest {
        //Given
        dataStoreRepository.saveEuroToDollarRate(Constant.EURO_TO_DOLLARS)
        advanceUntilIdle()

        //When
        val readRate: Double = dataStoreRepository.readEuroToDollarRate().first()
        advanceUntilIdle()

        //Then
        assertThat(readRate).isEqualTo(Constant.EURO_TO_DOLLARS)
    }

    @Test
    fun check_that_reading_dollar_to_euro_rate_is_correct() = runTest {
        //Given
        dataStoreRepository.saveEuroToDollarRate(Constant.DOLLARS_TO_EURO)
        advanceUntilIdle()

        //When
        val readRate: Double = dataStoreRepository.readEuroToDollarRate().first()
        advanceUntilIdle()

        //Then
        assertThat(readRate).isEqualTo(Constant.DOLLARS_TO_EURO)
    }

    @Test
    fun check_that_saving_and_reading_user_id_from_datastore_works_as_expected() = runTest{
        //Given
        val agent = SampleAgent.getSampleAgentList()[0]

        //When
        dataStoreRepository.saveAgentID(agent.id)
        val agentCheck = dataStoreRepository.readAgentID().first()
        advanceUntilIdle()

        //Then
        assertThat(agentCheck).isEqualTo(agent.id)
    }

    @Test
    fun check_that_currency_preference_is_saved_and_read_as_expected() = runTest {
        //Given
        val currencyPref = true

        //When
        dataStoreRepository.saveCurrencyPreference(currencyPref)
        val currencyPrefCheck = dataStoreRepository.readCurrencyPreference().first()
        advanceUntilIdle()

        //Then
        assertThat(currencyPrefCheck).isEqualTo(currencyPref)
    }

    @Test
    fun check_that_notification_preference_is_saved_and_read_as_expected() = runTest {
        //Given
        val notificationPref = true

        //When
        dataStoreRepository.saveNotificationPreference(notificationPref)
        val notificationPrefCheck = dataStoreRepository.readNotificationPreference().first()
        advanceUntilIdle()

        //Then
        assertThat(notificationPrefCheck).isEqualTo(notificationPref)
    }

}