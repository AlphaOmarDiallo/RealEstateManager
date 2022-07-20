package com.example.realestatemanager.data.repository.currencyAPI

import com.example.realestatemanager.MockResponseFileReader
import com.example.realestatemanager.data.model.usdToEur.UsdToEurRate
import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyAPIRepositoryImpTest {

    private val server: MockWebServer = MockWebServer()
    private val mockWebserverPort = 8080
    lateinit var abstractAPI: RetrofitAbstractAPI
    lateinit var currencyAPIRepositoryImp: CurrencyAPIRepositoryImp


    @Before
    fun init() {
        server.start(mockWebserverPort)

        abstractAPI = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAbstractAPI::class.java)

        currencyAPIRepositoryImp = CurrencyAPIRepositoryImp(abstractAPI)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun get_euro_rate_success() {

        runBlocking {
            server.apply {
                enqueue(MockResponse().setBody(MockResponseFileReader("assets/get_eur_rate_success_response.json").content))
            }
        }
        var response: Response<UsdToEurRate>?

        runBlocking { response = currencyAPIRepositoryImp.convertUSDtoEUR() }

        assertThat(response).isNotNull()


    }

}