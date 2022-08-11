package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.viewmodel.PropertyListViewModel
import com.example.realestatemanager.domain.*
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PropertyListFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var selectedProperty: Property
    private lateinit var windowInfo: WindowInfo
    private var currencyEuro by Delegates.notNull<Boolean>()
    private var dollarToEuroRate by Delegates.notNull<Double>()
    private val viewModel: PropertyListViewModel by viewModels()
    private var propertyList: List<Property> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.pList.observe(requireActivity()) {
            if (propertyList.isEmpty()) {
                propertyList = it
                Log.i(TAG, "onCreateView: list was empty")
            } else if (propertyList.isNotEmpty()) {
                if (it.size == propertyList.size) {
                    Log.i(TAG, "onCreateView: list was the same size")
                } else {
                    propertyList = it
                    viewModel.getPropertyList()
                    Log.i(TAG, "onCreateView: list was not the same size")
                }
            }
        }
        return ComposeView(requireContext()).apply {
            setContent {
                navController = findNavController()
                windowInfo = rememberWindowInfo()
                currencyEuro = viewModel.currencyEuro.value
                observeCurrencyPref()
                dollarToEuroRate = viewModel.dollarToEuroRate.value
                selectedProperty = viewModel.property.value
                Log.e(TAG, "onCreateView: property")

                var compose by remember { mutableStateOf(false) }

                viewModel.pList.observe(requireActivity()) {
                    if (propertyList.isEmpty()) {
                        propertyList = it
                        Log.i(TAG, "onCreateView: list was empty")
                    } else if (propertyList.isNotEmpty()) {
                        if (it.size == propertyList.size) {
                            compose = true
                            Log.i(TAG, "onCreateView: list was the same size")
                        } else {
                            propertyList = it
                            viewModel.getPropertyList()
                            Log.i(TAG, "onCreateView: list was not the same size")
                        }
                    }
                }

                if (compose){
                    Log.e(TAG, "onCreateView: here composing")
                    screenWidth(list = propertyList)
                }

            }
        }
    }

    @Composable
    fun screenWidth(list: List<Property>) {
        viewModel.getPropertyList()

        if (list.isNotEmpty()) Log.e(TAG, "screenWidth: ${list.last().price}")

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expended) {

            RealEstateManagerTheme {
                ExpendedScreen(list)
            }
        } else {
            RealEstateManagerTheme {
                PropertyList(list)
            }
        }
    }

    @Composable
    fun ExpendedScreen(propertyList: List<Property>) {
        Row {
            Surface(modifier = Modifier.fillMaxWidth(0.4f)) {

                PropertyList(propertyList)
            }
            Surface(modifier = Modifier.fillMaxWidth()) {
                val action =
                    PropertyListFragmentDirections.actionPropertyListFragmentToMortgageCalculatorFragment(
                        selectedProperty.price
                    )
                val action2 =
                    PropertyListFragmentDirections.actionPropertyListFragmentToCreateModifyFragment(
                        selectedProperty
                    )

                viewModel.getListInternalPhoto(requireContext())
                val listPhoto = selectedProperty.photoIDList

                Scaffold(
                    property = selectedProperty,
                    navController = navController,
                    navDirections = action,
                    navDirections2 = action2,
                    euro = currencyEuro,
                    dollarToEuroRate = dollarToEuroRate,
                    listPhoto = listPhoto
                )
            }
        }
    }

    @Composable
    fun TopButtons() {
        val action = PropertyListFragmentDirections.actionPropertyListFragmentToGoogleMapsFragment()
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .fillMaxSize(0.08f)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                IconButton(onClick = { navController.navigate(action) }) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    navigateToSearchFragment()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = ""
                    )
                }

                IconButton(onClick = {
                    navigateToCreateFragment()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    }

    @Composable
    fun PropertyList(propertyList: List<Property>) {

        Surface(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Column {
                TopButtons()
                ListOfProperty(propertyList = propertyList)
            }

        }

    }

    @Composable
    fun ListOfProperty(propertyList: List<Property>) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            items(propertyList) { property ->
                PropertyItem(property = property)
            }
        }
    }

    @Composable
    fun PropertyItem(property: Property) {
        Card(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            elevation = 10.dp
        ) {
            CardContent(property)
        }
    }

    @Composable
    fun CardContent(property: Property) {
        var expended by remember { mutableStateOf(false) }
        var euro by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .clickable {
                    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expended) {
                        viewModel.updateSelectedProperty(property)
                    } else {
                        val action =
                            PropertyListFragmentDirections.actionPropertyListFragmentToPropertyDetail(
                                property.id
                            )
                        navController.navigate(action)
                    }
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Image(
                    painter = rememberImagePainter(Uri.parse(property.photoIDList[0])),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    TextPropertyType(
                        propertyType = property.type,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary
                    )
                    TextNeighbourhoodAndCity(
                        neighbourhood = property.neighbourhood,
                        city = property.city
                    )
                    if (euro) {
                        TextPrice(
                            price = priceToEuro(property.price),
                            euro = currencyEuro,
                            dollarToEuroRate = dollarToEuroRate
                        )
                    } else {
                        TextPrice(
                            price = property.price,
                            euro = currencyEuro,
                            dollarToEuroRate = dollarToEuroRate
                        )
                    }

                }

                if (property.saleStatus) {
                    Text(text = "Sold", color = MaterialTheme.colors.error)
                }
                IconButton(onClick = {
                    euro = !euro
                    currencyEuro = euro
                }) {
                    Icon(
                        imageVector = Icons.Filled.Money,
                        contentDescription = if (expended) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(R.string.show_more)
                        }

                    )
                }
                if (windowInfo.screenWidthInfo != WindowInfo.WindowType.Expended) {
                    IconButton(onClick = { expended = !expended }) {
                        Icon(
                            imageVector = if (expended) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (expended) {
                                stringResource(R.string.show_less)
                            } else {
                                stringResource(R.string.show_more)
                            }
                        )
                    }
                }
            }
            if (expended) {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.Start
                ) {
                    TextAddress(address = property.address)
                    PropertyAttributes(
                        surface = property.surface,
                        rooms = property.numberOfRooms,
                        bedRooms = property.numberOfBedrooms,
                        bathRoom = property.numberOfBathrooms
                    )
                }
            }
        }
    }

/*    @Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(showBackground = true)
    @Composable
    fun FragmentPreview() {
        RealEstateManagerTheme {
            PropertyList()
        }
    }*/

    private fun navigateToSearchFragment() {
        val action = PropertyListFragmentDirections.actionPropertyListFragmentToSearchFragment()
        navController.navigate(action)
    }

    private fun navigateToCreateFragment() {
        val action =
            PropertyListFragmentDirections.actionPropertyListFragmentToCreateModifyFragment(null)
        navController.navigate(action)
    }

    private fun observeCurrencyPref() {
        viewModel.currencyEuroL.observe(viewLifecycleOwner, this::updateCurrencyL)
    }

    private fun updateCurrencyL(pref: Boolean) {
        currencyEuro = pref
    }

    private fun priceToEuro(price: Int): Int {
        return (price * dollarToEuroRate).toInt()
    }

}