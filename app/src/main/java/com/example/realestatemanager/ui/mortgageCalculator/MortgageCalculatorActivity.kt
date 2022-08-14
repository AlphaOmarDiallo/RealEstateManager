package com.example.realestatemanager.ui.mortgageCalculator

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.data.viewmodel.MortgageCalculatorViewModel
import com.example.realestatemanager.databinding.ActivityMortgageCalculatorBinding
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MortgageCalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMortgageCalculatorBinding
    private lateinit var viewModel: MortgageCalculatorViewModel

    private var propertyPrice by Delegates.notNull<Int>()
    private var mortgageRate by Delegates.notNull<Double>()
    private var mortgageLength by Delegates.notNull<Int>()
    private var monthlyPrice by Delegates.notNull<Int>()
    private var monthlyPriceEur by Delegates.notNull<Int>()
    private var priceList: MutableList<Int> = mutableListOf()
    private var downPayment = 0
    private var downPaymentEuro = 0
    private var euroToDollarRate = Constant.EURO_TO_DOLLARS
    private var dollarToEuroRate = Constant.DOLLARS_TO_EURO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMortgageCalculatorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[MortgageCalculatorViewModel::class.java]
        initialSetup()
        reactToDataChange()
    }

    /**
     * Sequences
     */
    private fun initialSetup() {
        readCurrencies()
        observingCurrencyRates()
        getPriceOfAllListedProperty(SampleProperties.samplePropertyList)
        setPropertyPrice()
        setMortgageRate()
        setMortgageLength()
        setMonthlyPayment()
        setAutocompleteTextView()
    }

    /**
     * Get the list of properties and sort it
     */
    //TODO get the list of property

    private fun getPriceOfAllListedProperty(properties: List<Property>) {
        for (property in properties) {
            priceList.add(property.price)
        }
        return priceList.sortDescending()
    }

    /**
     * Set views and vars
     */

    private fun setPropertyPrice() {
        propertyPrice = priceList.min()
        Log.d(TAG, "setPropertyPrice: $propertyPrice")
    }

    private fun setMortgageRate() {
        mortgageRate = (binding.sliderRate.value / 100).toDouble()
        setupRateValue(binding.sliderRate.value)
    }

    private fun setMortgageLength() {
        mortgageLength = (binding.sliderYears.value).toInt()
        setupYearValue(mortgageLength)
    }

    private fun setAutocompleteTextView() {
        val adapter = ArrayAdapter(this, R.layout.list_item, priceList)
        (binding.tvPropertyAmount.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.autoCompleteTextView.setText(priceList.min().toString(), false)
    }

    private fun readCurrencies() {
        viewModel.readCurrencies()
    }

    /**
     * Data change behavior
     */

    private fun reactToDataChange() {
        listenToSliderYearEvents()
        listenToSliderRateEvents()
        listenToDownPaymentChange()
        reactToPriceChange()
    }

    private fun reactToPriceChange() {
        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ ->
                propertyPrice = priceList[p2]
                setMonthlyPayment()
            }
    }

    private fun listenToSliderYearEvents() {
        binding.sliderYears.addOnChangeListener { _, _, _ ->
            setMortgageLength()
            setMonthlyPayment()
        }
    }

    private fun listenToSliderRateEvents() {
        binding.sliderRate.addOnChangeListener { _, _, _ ->
            setMortgageRate()
            setMonthlyPayment()
        }
    }

    private fun listenToDownPaymentChange() {
        binding.tiedDownPayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    downPayment =
                        if (p0 != null && p0.toString().toInt() != 0) p0.toString().toInt() else 0
                    downPaymentEuro = (downPayment * dollarToEuroRate).toInt()
                    downPaymentValueEur(downPaymentEuro)
                } catch (e: Exception) {
                    Log.w(TAG, "onTextChanged: ${e.message}")
                }
                setMonthlyPayment()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    /**
     * Observing currency rates
     */

    private fun observingCurrencyRates() {
        viewModel.eurToDollarRate.observe(this, this::updateEuroTODollarRate)
        viewModel.usdToEurRate.observe(this, this::updateDollarToEuroRate)
    }

    private fun updateEuroTODollarRate(rate: Double) {
        euroToDollarRate = rate
    }

    private fun updateDollarToEuroRate(rate: Double) {
        dollarToEuroRate = rate
    }

    /**
     * Reusable
     */

    private fun setMonthlyPayment() {
        val price = propertyPrice - downPayment

        monthlyPrice =
            viewModel.getMortgageMonthlyPaymentFee(price.toDouble(), mortgageRate, mortgageLength)
        binding.tvMonthlyPrice.text = "$ $monthlyPrice"

        monthlyPriceEur = (monthlyPrice * dollarToEuroRate).toInt()
        binding.tvMonthlyPriceEuro.text = "€ $monthlyPriceEur"

        totalInvestmentCost()
    }

    private fun totalInvestmentCost() {
        val totalCost = viewModel.getTotalInvestmentCost(monthlyPrice, mortgageLength)
        val totalCostEur = (viewModel.getTotalInvestmentCost(
            monthlyPrice,
            mortgageLength
        ) * dollarToEuroRate).toInt()
        binding.tvTotalInvestmentCost.text =
            getString(R.string.total_investment_cost, totalCost, totalCostEur)
    }

    private fun setupYearValue(years: Int) {
        binding.tvMortgageLengthValue.text = getString(R.string.mortgage_length_in_years, years)
    }

    private fun setupRateValue(rate: Float) {
        binding.tvMortgageRateValue.text = "$rate %"
    }

    private fun downPaymentValueEur(value: Int?) {
        binding.tiedDownPaymentEuro.setText("$value")
    }
}