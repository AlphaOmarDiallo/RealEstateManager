package com.example.realestatemanager.data.repository.dataStore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.ui.MainActivity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DataStoreRepositoryImpTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun dataStoreRepository_is_not_null() = assertThat(dataStoreRepository).isNotNull()

    @Test
    fun check_that_saved_euro_to_dollar_rate_is_correct() {
        runBlocking { dataStoreRepository.saveEuroToDollarRate(Constant.EURO_TO_DOLLARS) }

        var savedRate = 0.0

        runBlocking { dataStoreRepository.readEuroToDollarRate().collect { savedRate = it } }

        val expectedRate = Constant.EURO_TO_DOLLARS

        assertThat(savedRate).isEqualTo(expectedRate)
    }

    @Test
    fun check_that_saved_dollar_to_euro_rate_is_correct(){
        runTest { dataStoreRepository.saveDollarToEuroRate(Constant.DOLLARS_TO_EURO) }

        var savedRate = 0.0
        runBlocking{dataStoreRepository.readDollarToEuroRate().collect { savedRate = it }}

        val expectedRate = Constant.DOLLARS_TO_EURO

        assertThat(savedRate).isEqualTo(expectedRate)
    }

}