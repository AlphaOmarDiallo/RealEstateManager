package com.example.realestatemanager.data.model.eurToUsd

data class EurToUsdRate(
    val base: String,
    val exchange_rates: ExchangeRates,
    val last_updated: Int
)