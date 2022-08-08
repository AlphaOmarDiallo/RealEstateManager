package com.example.realestatemanager.domain

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import coil.compose.AsyncImage
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.InternalStoragePhoto
import com.example.realestatemanager.data.model.Property
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object PropertyDetailSharedComposable {

    @Composable
    fun Scaffold(
        property: Property,
        navController: NavController,
        navDirections: NavDirections,
        navDirections2: NavDirections,
        euro: Boolean,
        dollarToEuroRate: Double,
        listInternalPhoto: List<InternalStoragePhoto>
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    price = property.price,
                    navController = navController,
                    navDirections1 = navDirections,
                    navDirections2 = navDirections2,
                    euro = euro,
                    dollarToEuroRate = dollarToEuroRate
                )
            },
        ) {
            Box(modifier = Modifier.padding(it)) {
                PropertyInDetail(property = property, list = listInternalPhoto)
            }
        }
    }

    @Composable
    fun PropertyInDetail(property: Property, list: List<InternalStoragePhoto>) {
        LazyColumn(
            modifier = Modifier.padding(SharedComposable.largePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PropertyMainImage(propertyMainPhoto = property.photo!![0])
            }
            item {
                Box(modifier = Modifier.padding(vertical = SharedComposable.mediumPadding)) {
                    PropertyTitle(
                        type = property.type,
                        neighbourhood = property.neighbourhood,
                        city = property.city
                    )
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = SharedComposable.mediumPadding)) {
                    SharedComposable.TextAddress(
                        address = property.address,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = SharedComposable.mediumPadding)) {
                    SharedComposable.PropertyAttributes(
                        surface = property.surface,
                        rooms = property.numberOfRooms,
                        bedRooms = property.numberOfBedrooms,
                        bathRoom = property.numberOfBathrooms
                    )
                }
            }
            item {
                CardDescription(property = property)
            }
            item {
                Box(modifier = Modifier.padding(SharedComposable.mediumPadding)) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = SharedComposable.mediumPadding,
                            vertical = SharedComposable.largePadding
                        )
                    ) {
                        SharedComposable.SmallTitle(
                            title = "Photo gallery"
                        )
                        PropertyImageList(propertyPhoto = property.photo)
                    }
                }
            }
            item {
                AddMap(address = property.address)
            }
        }
    }

    @Composable
    fun PropertyMainImage(propertyMainPhoto: String) {
        DisplayPhoto(propertyPhoto = propertyMainPhoto, 400.dp, 400.dp)
    }

    @Composable
    fun PropertyTitle(type: String, neighbourhood: String, city: String) {
        Text(
            text = "$type in $neighbourhood of $city",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colors.secondary
        )
    }

    @Composable
    fun PropertyImageList(propertyPhoto: List<String>?) {
        if (propertyPhoto != null) {
            LazyRow(verticalAlignment = Alignment.CenterVertically) {
                items(propertyPhoto){
                    DisplayPhoto(propertyPhoto = it, boxHeight = 200.dp, imageHeight = 200.dp)
                }
            }
        }
    }

    @Composable
    fun DisplayPhoto(
        propertyPhoto: String,
        boxHeight: Dp,
        imageHeight: Dp,
        photoDescription: String? = null
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
            elevation = 5.dp
        ) {
            Box(modifier = Modifier.height(boxHeight)) {
                AsyncImage(
                    model = propertyPhoto,
                    contentDescription = "Property photo",
                    modifier = Modifier
                        .size(imageHeight)
                        .matchParentSize(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(id = R.drawable.house_placeholder)
                )
                if (photoDescription != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    SharedComposable.colorGradient,
                                    startY = 500f,
                                    endY = 1000f
                                ),
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(SharedComposable.mediumPadding),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Text(
                                text = photoDescription,
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CardDescription(property: Property) {
        var expended by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = SharedComposable.smallPadding)
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = SharedComposable.smallPadding)
                ) {

                    SharedComposable.SmallTitle("Description")

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
                if (!expended) {
                    PropertyDescription(propertyDescription = property.description, maxLines = 100)
                } else if (expended) {
                    PropertyDescription(propertyDescription = property.description, maxLines = 3)
                }
            }
        }
    }

    @Composable
    fun PropertyDescription(propertyDescription: String, maxLines: Int) {
        Text(
            text = propertyDescription,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(4.dp),
            maxLines = maxLines
        )
    }

    @Composable
    fun AddMap(address: String) {
        //var addressToLocation: Location? = viewModel.location.value

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SharedComposable.mediumPadding),
            shape = RoundedCornerShape(15.dp),
            elevation = 10.dp
        ) {
            val addressLatLng = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(addressLatLng, 10f)
            }
            GoogleMap(
                modifier = Modifier.size(300.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = addressLatLng),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
    }

    @Composable
    fun BottomBar(
        price: Int,
        navController: NavController,
        navDirections1: NavDirections,
        navDirections2: NavDirections,
        euro: Boolean,
        dollarToEuroRate: Double
    ) {
        Surface(
            elevation = 5.dp,
            modifier = Modifier
                .background(MaterialTheme.colors.onPrimary)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp)
            ) {
                Column {
                    Text(
                        text = if (euro) "€ ${(price * dollarToEuroRate).toInt()}" else "$ $price",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.clickable {
                            navController.navigate(navDirections1)
                        }
                    )
                    val rate = 2.00
                    val years = 30
                    val monthlyPayment = if (euro) MortgagePaymentUtil.monthlyPaymentMortgage(
                        (price.toDouble() * dollarToEuroRate),
                        rate,
                        years
                    ) else MortgagePaymentUtil.monthlyPaymentMortgage(price.toDouble(), rate, years)
                    if (euro) Text(text = "from €$monthlyPayment per month") else Text(text = "from $$monthlyPayment per month")
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(SharedComposable.mediumPadding)
                        .border(
                            2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = "Bills for payment",
                            modifier = Modifier.padding(horizontal = SharedComposable.smallPadding),
                            tint = MaterialTheme.colors.primaryVariant
                        )
                        Text(
                            text = "Found a buyer",
                            modifier = Modifier.padding(horizontal = SharedComposable.smallPadding),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
                IconButton(onClick = {
                    navigateToEditFragment(navDirections2, navController)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = ""
                    )
                }
            }
        }
    }

    private fun navigateToEditFragment(navDirections: NavDirections, navController: NavController){
        navController.navigate(navDirections)
    }
}