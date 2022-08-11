package com.example.realestatemanager.domain.provider

import android.content.ContentResolver
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.domain.provider.PropertyContentProvider.Companion.CONTENT_URI
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PropertyContentProviderTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localDatabase: LocalDatabase

    private lateinit var contentResolver: ContentResolver

    @Before
    fun init() {
        hiltRule.inject()
        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @After
    fun close() {
        localDatabase.agentDao().nukeTable()
        localDatabase.propertyDao().nukeTable()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_that_content_provider_gets_data() {
        runTest {
            localDatabase.propertyDao().insertProperty(SampleProperties.samplePropertyList[0])
            advanceUntilIdle()
        }
        val cursor = contentResolver.query(CONTENT_URI, null, null, null)

        Log.e("test", "test_that_content_provider_gets_data: ${cursor.toString()}")

        assertThat(cursor).isNotNull()

        assertThat(cursor!!.count).isEqualTo(1)

        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("address"))).contains(
            SampleProperties.samplePropertyList[0].address
        )
    }


}