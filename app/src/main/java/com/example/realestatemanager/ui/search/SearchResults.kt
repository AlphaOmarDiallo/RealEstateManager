package com.example.realestatemanager.ui.search

import android.content.ContentValues
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.viewmodel.SearchViewModel
import com.example.realestatemanager.domain.*
import com.example.realestatemanager.domain.composable.WindowInfo
import com.example.realestatemanager.domain.composable.rememberWindowInfo
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class SearchResults : Fragment() {

    private lateinit var windowInfo: WindowInfo
    private lateinit var navController: NavController
    private var currencyEuro by Delegates.notNull<Boolean>()
    private var dollarToEuroRate by Delegates.notNull<Double>()
    private val viewModel: SearchViewModel by viewModels()
    private var propertyList: List<Property> = listOf()
    private val args: SearchResultsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            setContent {
                windowInfo = rememberWindowInfo()
                navController = findNavController()
                dollarToEuroRate = viewModel.dollarToEuroRate.value
                currencyEuro = false

                RealEstateManagerTheme() {
                    propertyList = args.resultList.result
                    Log.e(ContentValues.TAG, "onCreateView: here composing")
                    ListOfProperty(propertyList)
                }
            }
        }

    }

    @Composable
    fun ListOfProperty(list: List<Property>) {
        Log.e(TAG, "ListOfProperty: here")
        propertyList = list
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
                    val action =
                        SearchResultsDirections.actionSearchResultsToPropertyDetail(
                            property.id
                        )
                    navController.navigate(action)
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
                    TextPrice(
                        price = property.price,
                        euro = currencyEuro,
                        dollarToEuroRate = dollarToEuroRate
                    )
                }

                if (property.saleStatus) {
                    Text(text = "Sold", color = MaterialTheme.colors.error)
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
}