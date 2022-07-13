package com.example.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.domain.SharedComposable
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class PropertyDetailFragment : Fragment() {
    private val propertyList = SampleProperties.samplePropertyList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PropertyDetail()
            }
        }
    }

    @Composable
    fun PropertyDetail() {
        RealEstateManagerTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.primaryVariant)
            ) {
                PropertyInDetail(property = propertyList[0])
            }
        }
    }

    @Composable
    fun PropertyInDetail(property: Property) {
        Column {
            SharedComposable.TextPropertyType(propertyType = property.type)
            Row {
                SharedComposable.TextNeighbourhoodAndCity(neighbourhood = property.neighbourhood, city = property.city)
                SharedComposable.TextPrice(price = property.price)
            }
            PropertyImageList(propertyPhoto = property.mainPhoto)
            PropertyDescription(propertyDescription = property.description)
            SharedComposable.PropertyAttributes(
                surface = property.surface,
                rooms = property.numberOfRooms,
                bedRooms = property.numberOfBedrooms,
                bathRoom = property.numberOfBathrooms)
            SharedComposable.TextAddress(address = property.address)
            AddMap(address = property.address)
        }
    }

    @Composable
    fun PropertyImageList(propertyPhoto: List<String>?) {
        if (propertyPhoto != null){
            LazyRow(verticalAlignment = Alignment.CenterVertically){
                items(propertyPhoto) {
                    for (photo in propertyPhoto){
                        DisplayPhoto(propertyPhoto = photo)
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayPhoto (propertyPhoto: String){
        AsyncImage(
            model = propertyPhoto,
            contentDescription = "Property photo"
        )
    }

    @Composable
    fun PropertyDescription(propertyDescription: String){
        Text(text = propertyDescription)
    }


    @Composable
    fun AddMap(address: String){

        val addressLatLng = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(addressLatLng, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = addressLatLng),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
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
            PropertyDetail()
        }
    }
}