package com.example.realestatemanager.data.model.usdToEur

data class UsdToEurRate(
    val lastUpdated: Int,
    val exchangeRates: ExchangeRates,
    val base: String
)