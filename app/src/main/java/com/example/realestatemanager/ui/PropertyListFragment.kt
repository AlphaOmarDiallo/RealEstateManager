package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.viewmodel.PropertyListViewModel
import com.example.realestatemanager.domain.PropertyDetailSharedComposable
import com.example.realestatemanager.domain.SharedComposable
import com.example.realestatemanager.domain.WindowInfo
import com.example.realestatemanager.domain.rememberWindowInfo
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
    private var propertyList = mutableListOf<Property>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                navController = findNavController()
                windowInfo = rememberWindowInfo()
                propertyList = viewModel.propertyList.toMutableStateList()
                currencyEuro = viewModel.currencyEuro.value
                Log.e(TAG, "onCreateView: euro: $currencyEuro")
                observeCurrencyPref()
                dollarToEuroRate = viewModel.dollarToEuroRate.value

                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expended) {
                    selectedProperty = viewModel.property.value
                    RealEstateManagerTheme {
                        ExpendedScreen()
                    }
                } else {
                    RealEstateManagerTheme {
                        PropertyList(propertyList)
                    }
                }
            }
        }
    }

    @Composable
    fun ExpendedScreen() {
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
                    PropertyListFragmentDirections.actionPropertyListFragmentToCreateModifyFragment(selectedProperty)

                viewModel.getListInternalPhoto(requireContext())
                val listInternalPhoto = viewModel.listInternalStoragePhoto.value

                PropertyDetailSharedComposable.Scaffold(
                    property = selectedProperty,
                    navController = navController,
                    navDirections = action,
                    navDirections2 = action2,
                    euro = currencyEuro,
                    dollarToEuroRate = dollarToEuroRate,
                    listInternalPhoto = listInternalPhoto
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
        //val propertyList = viewModel._propertyList

        Box(
            modifier = Modifier
                .fillMaxHeight(0.92f)
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
            modifier = Modifier.padding(vertical = 4.dp)
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
                    painter = painterResource(id = R.drawable.property_placeholder),
                    contentDescription = "Image of the property",
                    modifier = Modifier
                        //.size(100.dp)
                        .weight(1f)
                        .fillMaxSize(),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    SharedComposable.TextPropertyType(
                        propertyType = property.type,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary
                    )
                    SharedComposable.TextNeighbourhoodAndCity(
                        neighbourhood = property.neighbourhood,
                        city = property.city
                    )
                    if (euro) {
                        SharedComposable.TextPrice(
                            price = priceToEuro(property.price),
                            euro = currencyEuro,
                            dollarToEuroRate = dollarToEuroRate
                        )
                    } else {
                        SharedComposable.TextPrice(
                            price = property.price,
                            euro = currencyEuro,
                            dollarToEuroRate = dollarToEuroRate
                        )
                    }

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
                    SharedComposable.TextAddress(address = property.address)
                    SharedComposable.PropertyAttributes(
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