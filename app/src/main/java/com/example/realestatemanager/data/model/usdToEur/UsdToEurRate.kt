package com.example.realestatemanager.data.model.usdToEur

data class UsdToEurRate(
    val base: String,
    val exchange_rates: ExchangeRates,
    val last_updated: Int
)