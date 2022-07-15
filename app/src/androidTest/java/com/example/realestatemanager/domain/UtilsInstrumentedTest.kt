package com.example.realestatemanager

import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.data.repository.currencyAPI.CurrencyAPIRepository
import com.example.realestatemanager.domain.Utils
import com.example.realestatemanager.ui.MainActivity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class SettingsActivityTest {

    private lateinit var instrumentationContext: InstrumentationContext

    @Inject
    lateinit var currencyAPIRepository: CurrencyAPIRepository

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        instrumentationContext = InstrumentationContext()
        hiltRule.inject()
    }

    @Test
    fun repository_is_not_null() {
        assertThat(currencyAPIRepository).isNotNull()
    }

    @Test
    fun is_Internet_Connectivity_Checked() {
        assertThat(Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().targetContext)).isNotNull()
    }

    @Test
    fun is_internet_available_method_below_build_version_M() {
        assertThat(Utils.isInternetAvailableBuildVersionBelowM()).isNotNull()
    }

    @Test
    fun is_internet_available_method_above_build_version_M() {
        assertThat(Utils.isInternetAvailableBuildVersionCodAboveM(InstrumentationRegistry.getInstrumentation().targetContext)).isNotNull()
    }

    //Todo test app
}