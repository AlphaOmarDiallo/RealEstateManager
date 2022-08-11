package com.example.realestatemanager.ui.mortgageCalculator

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.data.viewmodel.MortgageCalculatorViewModel
import com.example.realestatemanager.databinding.FragmentMortgageCalculatorBinding
import com.example.realestatemanager.domain.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MortgageCalculatorFragment : Fragment() {

    private var rateKey = "rate key"

    private lateinit var binding: FragmentMortgageCalculatorBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MortgageCalculatorViewModel

    private val args: MortgageCalculatorFragmentArgs by navArgs()

    private var propertyPrice by Delegates.notNull<Int>()
    private var mortgageRate by Delegates.notNull<Double>()
    private var mortgageLength by Delegates.notNull<Int>()
    private var monthlyPrice by Delegates.notNull<Int>()
    private var monthlyPriceEur by Delegates.notNull<Int>()
    private var euroToDollarRate by Delegates.notNull<Double>()
    private var dollarToEuroRate = 0.00
    private var priceList: MutableList<Int> = mutableListOf()
    private var downPayment = 0
    private var downPaymentEuro = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMortgageCalculatorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MortgageCalculatorViewModel::class.java]
        if (savedInstanceState != null) dollarToEuroRate =
            savedInstanceState.getDouble(rateKey, Constant.DOLLARS_TO_EURO)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetup()
        reactToDataChange()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(rateKey, dollarToEuroRate)
    }


    /**
     * Sequences
     */
    private fun initialSetup() {
        readCurrencies()
        observingCurrencyRates()
        getArgsFromNav()
        getPriceOfAllListedProperty(SampleProperties.samplePropertyList)
        setPropertyPrice()
        setMortgageRate()
        setMortgageLength()
        setMonthlyPayment()
        setAutocompleteTextView()
    }

    /**
     * Get args from NavHost
     */

    private fun getArgsFromNav() {
        navController = findNavController()
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
        propertyPrice = if (args.propertyPrice != 0) args.propertyPrice else priceList.min()
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
        if (args.propertyPrice != 0) {
            binding.tvPropertyAmount.visibility = View.GONE
        } else {
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, priceList)
            (binding.tvPropertyAmount.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
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
        viewModel.eurToDollarRate.observe(requireActivity(), this::updateEuroTODollarRate)
        viewModel.usdToEurRate.observe(requireActivity(), this::updateDollarToEuroRate)
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