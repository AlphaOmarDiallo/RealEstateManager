package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.viewmodel.PropertyDetailViewModel
import com.example.realestatemanager.domain.Scaffold
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PropertyDetailFragment : Fragment() {

    private lateinit var navController: NavController
    private var currencyEuro by Delegates.notNull<Boolean>()
    private var dollarToEuroRate by Delegates.notNull<Double>()
    private val viewModel: PropertyDetailViewModel by viewModels()
    private val args: PropertyDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                navController = findNavController()
                currencyEuro = viewModel.currencyEuro.value
                Log.e(TAG, "onCreateView: euro: $currencyEuro")
                dollarToEuroRate = viewModel.dollarToEuroRate.value

                val propertyID = args.propertyID
                fetchPropertyData(propertyID)
                val property: Property = getDataFromViewModel()
                val action1 =
                    PropertyDetailFragmentDirections.actionPropertyDetailToMortgageCalculatorFragment(
                        property.price
                    )
                val action2 =
                    PropertyDetailFragmentDirections.actionPropertyDetailToCreateModifyFragment(
                        "null",
                        property
                    )

                viewModel.getListInternalPhoto(requireContext())

                val propertyPhoto = property.photoIDList

                Scaffold(
                    property = property,
                    navController = navController,
                    navDirections = action1,
                    navDirections2 = action2,
                    euro = currencyEuro,
                    dollarToEuroRate = dollarToEuroRate,
                    listPhoto = propertyPhoto
                )
            }
        }
    }

    /**
     * Getting data from viewModel
     */

    private fun getDataFromViewModel(): Property {
        return viewModel.property.value
    }

    /**
     * Retrieving property
     */

    private fun fetchPropertyData(id: Int) {
        viewModel.getProperty(id)
    }

}