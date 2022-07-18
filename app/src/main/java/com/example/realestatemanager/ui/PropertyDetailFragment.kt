package com.example.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.realestatemanager.R
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
    //private val viewModel: PropertyDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                //getDataFromViewModel(propertyList[0])
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
                    .background(color = MaterialTheme.colors.primary)
            ) {
                PropertyInDetail(property = propertyList[0])
            }
        }
    }

    @Composable
    fun PropertyInDetail(property: Property) {
        LazyColumn(modifier = Modifier.padding(20.dp)) {
            item {
                SharedComposable.TextPropertyType(
                    propertyType = property.type,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Area: ${property.neighbourhood}",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = "City: ${property.city}",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    SharedComposable.TextPrice(
                        price = property.price,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h4
                    )
                }
            }
            item {
                PropertyImageList(propertyPhoto = property.photo)
            }
            item {
                PropertyDescription(propertyDescription = property.description)
            }
            item {
                SharedComposable.PropertyAttributes(
                    surface = property.surface,
                    rooms = property.numberOfRooms,
                    bedRooms = property.numberOfBedrooms,
                    bathRoom = property.numberOfBathrooms
                )
            }
            item{
                SharedComposable.TextAddress(address = property.address)
            }
            item {
                AddMap(address = property.address)
            }
        }
    }

    @Composable
    fun PropertyImageList(propertyPhoto: List<String>?) {
        if (propertyPhoto != null) {
            LazyRow(verticalAlignment = Alignment.CenterVertically) {
                items(propertyPhoto) {
                    DisplayPhoto(propertyPhoto = it)
                }
            }
        }
    }

    @Composable
    fun DisplayPhoto(propertyPhoto: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.error)
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                AsyncImage(
                    model = propertyPhoto,
                    contentDescription = "Property photo",
                    modifier = Modifier.size(250.dp),
                    contentScale = ContentScale.FillBounds,
                    error = painterResource(id = R.drawable.house_placeholder)
                )
            }
        }
    }

    @Composable
    fun PropertyDescription(propertyDescription: String) {
        Text(
            text = propertyDescription,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1,
        )
    }


    @Composable
    fun AddMap(address: String) {
        //var addressToLocation: Location? = viewModel.location.value

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.secondary)
        ) {
            val addressLatLng = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(addressLatLng, 10f)
            }
            GoogleMap(
                modifier = Modifier.size(200.dp),
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

    @Preview(showBackground = true)
    @Composable
    fun Function() {
        RealEstateManagerTheme {
            PropertyImageList(propertyPhoto = propertyList[0].photo)
        }
    }

    /**
     * Getting data from viewModel
     */


    private fun getDataFromViewModel(property: Property) {
        //viewModel.addressToLocation(property.address)
    }
}