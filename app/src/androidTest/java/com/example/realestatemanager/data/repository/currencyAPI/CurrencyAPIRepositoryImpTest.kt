package com.example.realestatemanager.data.repository.currencyAPI

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CurrencyAPIRepositoryImpTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var currencyAPIRepository: CurrencyAPIRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun currency_API_Repository_is_injected(){
        assertThat(currencyAPIRepository).isNotNull()
    }

    @Test
    fun mock_server_is_running(){
        assertThat(mockWebServer).isNotNull()
    }

    @Test
    fun currency_api_get_euro_rate_should_fetch_rate_correctly_given_200_response(){

    }
}