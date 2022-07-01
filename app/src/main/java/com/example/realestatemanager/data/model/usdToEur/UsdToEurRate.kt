package com.example.realestatemanager.data.model.usdToEur;

public class UsdToEurRate {
	private int lastUpdated;
	private ExchangeRates exchangeRates;
	private String base;

	public int getLastUpdated(){
		return lastUpdated;
	}

	public ExchangeRates getExchangeRates(){
		return exchangeRates;
	}

	public String getBase(){
		return base;
	}
}
