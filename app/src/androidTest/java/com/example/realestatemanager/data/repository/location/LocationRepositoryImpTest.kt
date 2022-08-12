package com.example.realestatemanager.data.repository.location

import android.location.Location
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LocationRepositoryImpTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var locationRepositoryImp: LocationRepositoryImp

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun get_Office_location_returns_correct_location() {
        val officeLocation: Location = locationRepositoryImp.getOfficeLocation()
        val expectedLocation: Location = Location("expected")
        expectedLocation.latitude = (-73.98956985396345f).toDouble()
        expectedLocation.longitude = (40.741694549848404f).toDouble()
        assertThat(officeLocation.latitude).isEqualTo(expectedLocation.latitude)
        assertThat(officeLocation.longitude).isEqualTo(expectedLocation.longitude)
    }
}