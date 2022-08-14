package com.example.realestatemanager.domain.composable

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import coil.compose.rememberImagePainter
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.PlaceDetail
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.domain.utils.MortgagePaymentUtil
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*

@Composable
fun Scaffold(
    property: Property,
    navController: NavController,
    navDirections: NavDirections,
    navDirections2: NavDirections,
    euro: Boolean,
    dollarToEuroRate: Double,
    listPhoto: List<String>
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                property = property,
                navController = navController,
                navDirections1 = navDirections,
                navDirections2 = navDirections2,
                euro = euro,
                dollarToEuroRate = dollarToEuroRate
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            PropertyInDetail(property = property, list = listPhoto)
        }
    }
}

@Composable
fun PropertyInDetail(property: Property, list: List<String>) {
    LazyColumn(
        modifier = Modifier.padding(largePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (list.isNotEmpty()) {
            item {
                PropertyMainImage(propertyMainPhoto = property.photoIDList[0])
            }
        }
        item {
            Box(modifier = Modifier.padding(vertical = mediumPadding)) {
                PropertyTitle(
                    type = property.type,
                    neighbourhood = property.neighbourhood,
                    city = property.city
                )
            }
        }
        item {
            Box(modifier = Modifier.padding(vertical = mediumPadding)) {
                TextAddress(
                    address = property.address,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            Box(modifier = Modifier.padding(vertical = mediumPadding)) {
                PropertyAttributes(
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
            Box(modifier = Modifier.padding(mediumPadding)) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = mediumPadding,
                        vertical = largePadding
                    )
                ) {
                    SmallTitle(
                        title = "Photo gallery"
                    )
                    PropertyImageList(propertyPhoto = property.photoIDList)
                }
            }
        }
        item {
            AddMap(
                latLng = property.latLng,
                address = property.address,
                type = property.type,
                list = property.listPlaceDetail
            )
        }
        item {
            Text(text = "3 km radius", style = MaterialTheme.typography.h6)
            Column {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "Close to school = ${if (property.closeToSchool) "yes" else "no"}"
                )
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "Close to parks = ${if (property.closeToPark) "yes" else "no"}"
                )
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "Close to shops = ${if (property.closeToShops) "yes" else "no"}"
                )
            }
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
            items(propertyPhoto) {
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
            Image(
                painter = rememberImagePainter(Uri.parse(propertyPhoto)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(imageHeight)
            )

            if (photoDescription != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colorGradient,
                                startY = 500f,
                                endY = 1000f
                            ),
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(mediumPadding),
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
                .padding(horizontal = smallPadding)
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
                    .padding(vertical = smallPadding)
            ) {

                SmallTitle("Description")

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
fun AddMap(latLng: LatLng, address: String, type: String, list: List<PlaceDetail>?) {

    val markerClick: (Marker) -> Boolean = {
        Log.d(TAG, "${it.title} was clicked")
        false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        shape = RoundedCornerShape(15.dp),
        elevation = 10.dp
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, 16f)
        }
        GoogleMap(
            modifier = Modifier.size(300.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = latLng),
                title = type,
                snippet = address
            )

            if (!list.isNullOrEmpty()) {
                for (place in list) {
                    MarkerInfoWindowContent(
                        state = MarkerState(
                            position = LatLng(
                                place.placeLat.toDouble(),
                                place.placeLng.toDouble()
                            )
                        ),
                        title = place.placeName,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        onClick = markerClick,
                        snippet = place.placeType
                    ) {
                        Column {
                            Text(text = it.title ?: "Title", style = MaterialTheme.typography.body1,color = Color.Blue)
                            Text(text = it.snippet ?: "Title", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    property: Property,
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
                    text = if (euro) "€ ${(property.price * dollarToEuroRate).toInt()}" else "$ ${property.price}",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.clickable {
                        navController.navigate(navDirections1)
                    }
                )
                if (property.saleStatus) {
                    Text(text = "SOLD")
                } else {
                    val rate = 2.00
                    val years = 30
                    val monthlyPayment = if (euro) MortgagePaymentUtil.monthlyPaymentMortgage(
                        (property.price.toDouble() * dollarToEuroRate),
                        rate,
                        years
                    ) else MortgagePaymentUtil.monthlyPaymentMortgage(
                        property.price.toDouble(),
                        rate,
                        years
                    )
                    if (euro) Text(text = "from €$monthlyPayment per month") else Text(text = "from $$monthlyPayment per month")
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

private fun navigateToEditFragment(navDirections: NavDirections, navController: NavController) {
    navController.navigate(navDirections)
}