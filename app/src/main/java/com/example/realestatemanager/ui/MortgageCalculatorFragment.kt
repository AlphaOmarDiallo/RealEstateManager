package com.example.realestatemanager.ui

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.databinding.FragmentMortgageCalculatorBinding
import com.example.realestatemanager.domain.MortgagePaymentUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MortgageCalculatorFragment : Fragment() {

    private lateinit var binding: FragmentMortgageCalculatorBinding
    private lateinit var navController: NavController

    private val args: MortgageCalculatorFragmentArgs by navArgs()

    private var propertyPrice by Delegates.notNull<Int>()
    private var mortgageRate by Delegates.notNull<Double>()
    private var mortgageLength by Delegates.notNull<Int>()
    private var monthlyPrice by Delegates.notNull<Int>()
    private var priceList: MutableList<Int> = mutableListOf()

    private var currencyDollar = true
    private var downPayment = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMortgageCalculatorBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_mortgage_calculator, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetup()
        reactToDataChange()
    }

    /**
     * Sequences
     */
    private fun initialSetup() {
        getArgsFromNav()
        getPriceOfAllListedProperty(SampleProperties.samplePropertyList)
        setPropertyPrice()
        setMortgageRate()
        setMortgageLength()
        setDownPayment()
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
        Log.d(TAG, "getPriceOfAllListedProperty: $priceList")
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
        Log.d(TAG, "setMortgageRate: $mortgageRate")
    }

    private fun setMortgageLength() {
        mortgageLength = (binding.sliderYears.value).toInt()
        setupYearValue(mortgageLength)
        Log.d(TAG, "setMortgageLength: $mortgageLength")
    }

    private fun setAutocompleteTextView() {
        if (args.propertyPrice != 0) {
            binding.tvPropertyAmount.visibility = View.GONE
        } else {
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, priceList)
            (binding.tvPropertyAmount.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            Log.d(TAG, "setAutocompleteTextView: ${priceList.indexOf(propertyPrice)}")
        }
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
        binding.sliderYears.addOnChangeListener { slider, value, fromUser ->
            setMortgageLength()
            setMonthlyPayment()
        }
    }

    private fun listenToSliderRateEvents() {
        binding.sliderRate.addOnChangeListener { slider, value, fromUser ->
            setMortgageRate()
            setMonthlyPayment()
        }
    }

    private fun listenToDownPaymentChange() {
        binding.tiedDownPayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    downPayment = if (p0 != null && p0.toString().toInt() != 0) p0.toString().toInt() else 0
                } catch (e: Exception) {
                    Log.w(TAG, "onTextChanged: ${e.message}", )
                }
                Log.d(TAG, "onTextChanged: $downPayment")
                setMonthlyPayment()
            }

            override fun afterTextChanged(p0: Editable?) {
                
            }
        })
    }

    /**
     * Reusable
     */

    private fun setMonthlyPayment() {
        val price = propertyPrice - downPayment
        monthlyPrice = MortgagePaymentUtil.monthlyPaymentMortgage(
            price.toDouble(),
            mortgageRate,
            mortgageLength
        )
        //val monthlyPriceInEuro =
        binding.tvMonthlyPrice.text = "$$monthlyPrice"
        totalInvestmentCost()
    }

    private fun totalInvestmentCost() {
        val totalCost = monthlyPrice * (mortgageLength * 12)
        binding.tvTotalInvestmentCost.text = "Total investment cost is $$totalCost"
    }

    private fun setDownPayment() {
        binding.tiedDownPayment.setText(downPayment.toString())
    }

    private fun setupYearValue(years: Int) {
        binding.tvMortgageLengthValue.text = ("$years years")
    }

    private fun setupRateValue(rate: Float) {
        binding.tvMortgageRateValue.text = "$rate %"
    }

}