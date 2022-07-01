package com.example.realestatemanager.domain

import com.example.realestatemanager.data.remoteData.RetrofitAbstractAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_CURRENCY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RetrofitAbstractAPI by lazy {
        retrofit.create(RetrofitAbstractAPI::class.java)
    }
}