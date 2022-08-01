package com.example.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.data.viewmodel.PropertyDetailViewModel
import com.example.realestatemanager.domain.PropertyDetailSharedComposable
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertyDetailFragment : Fragment() {

    private val viewModel: PropertyDetailViewModel by viewModels()
    private lateinit var navController: NavController
    private val args: PropertyDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                navController = findNavController()
                val propertyID = args.propertyID
                fetchPropertyData(propertyID)
                val property: Property = getDataFromViewModel()
                PropertyDetailSharedComposable.Scaffold(
                    property = SampleProperties.samplePropertyList[0],
                    navController = navController,
                    navDirections = PropertyDetailFragmentDirections.actionPropertyDetailToMortgageCalculatorFragment(
                        SampleProperties.samplePropertyList[0].price
                    )
                )
            }
        }
    }

    @Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(showBackground = true)
    @Composable
    fun FragmentPreview() {
        RealEstateManagerTheme {
            PropertyDetailSharedComposable.Scaffold(
                property = SampleProperties.samplePropertyList[0],
                navController = navController,
                navDirections = PropertyDetailFragmentDirections.actionPropertyDetailToMortgageCalculatorFragment(
                    SampleProperties.samplePropertyList[0].price
                )
            )
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