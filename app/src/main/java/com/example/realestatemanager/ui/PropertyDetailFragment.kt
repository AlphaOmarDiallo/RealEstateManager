package com.example.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                    .background(color = MaterialTheme.colors.background)
            ) {
                PropertyInDetail(property = propertyList[0])
            }
        }
    }

    @Composable
    fun PropertyInDetail(property: Property) {
        LazyColumn(modifier = Modifier.padding(20.dp)) {
            item {
                PropertyImageList(propertyPhoto = property.photo)
            }
            item {
                CardDescription(property = property)
            }
            item {
                SharedComposable.PropertyAttributes(
                    surface = property.surface,
                    rooms = property.numberOfRooms,
                    bedRooms = property.numberOfBedrooms,
                    bathRoom = property.numberOfBathrooms
                )
            }
            item {
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
            elevation = 5.dp
        ) {
            Box(modifier = Modifier.height(400.dp)) {
                AsyncImage(
                    model = propertyPhoto,
                    contentDescription = "Property photo",
                    modifier = Modifier.size(450.dp),
                    contentScale = ContentScale.FillBounds,
                    error = painterResource(id = R.drawable.house_placeholder)
                )
            }
        }
    }

    @Composable
    fun PropertyTitle(type: String, neighbourhood: String, city: String){
        Text(
            text = "$type in $neighbourhood of $city",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colors.secondary
        )
    }

    @Composable
    fun CardDescription(property: Property) {
        var expended by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            //elevation = 10.dp,
            backgroundColor = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
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
                        .padding(8.dp)
                ) {

                    PropertyTitle(type = property.type, neighbourhood = property.neighbourhood, city = property.city)

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
                if (expended) {
                    PropertyDescription(propertyDescription = property.description)
                }
            }
        }
    }

    @Composable
    fun PropertyDescription(propertyDescription: String) {
        Text(
            text = propertyDescription,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(4.dp),
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
            CardDescription(property = propertyList[0])
        }
    }

    /**
     * Getting data from viewModel
     */


    private fun getDataFromViewModel(property: Property) {
        //viewModel.addressToLocation(property.address)
    }

}