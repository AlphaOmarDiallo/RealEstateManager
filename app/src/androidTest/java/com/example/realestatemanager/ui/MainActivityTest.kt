package com.example.realestatemanager.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.R
import com.example.realestatemanager.data.repository.connectivity.ConnectivityRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    @Rule(order = 1)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Rule(order = 2)
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    private val context = InstrumentationRegistry.getInstrumentation().context

    @Before
    fun init() {
        hiltAndroidRule.inject()
    }

    @Test
    fun check_that_snackBar_is_giving_internet_connection_status_on_startup() {
        val connectionStatus = connectivityRepository.isInternetAvailable(context)
        if (connectionStatus) {
            onView(withId(R.id.ivOffline)).check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.ivOffline)).check(matches(isDisplayed()))
        }
    }
}