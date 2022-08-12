package com.example.realestatemanager.data.repository.permission

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PermissionRepositoryImpTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var permissionRepositoryImp: PermissionRepositoryImp

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun tearDown() {

    }

    @Test
    fun check_if_permissions_are_granted(){
        val result = permissionRepositoryImp.hasLocationPermission(context)
        assertThat(result).isNotNull()
    }
}