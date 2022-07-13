package com.example.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme

class PropertyListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApp()
            }
        }
    }

    @Composable
    fun MyApp() {
        RealEstateManagerTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.primaryVariant)
            ) {
                ListOfProperty(propertyList = SampleProperties.samplePropertyList)
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
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            CardContent(property)
        }
    }

    @Composable
    fun CardContent(property: Property) {
        var expended by remember { mutableStateOf(false) }
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
                .clickable { Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show() }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.propertyplaceholder),
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
                    TextPropertyType(propertyType = property.type)
                    TextNeighbourhoodAndCity(
                        neighbourhood = property.neighbourhood,
                        city = property.city
                    )
                    TextPrice(price = property.price)
                }

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

    @Composable
    fun TextPropertyType(propertyType: String) {
        Text(
            text = propertyType,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun TextNeighbourhoodAndCity(neighbourhood: String, city: String) {
        Text(
            text = "$neighbourhood, $city",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondary
        )
    }

    @Composable
    fun TextPrice(price: Int) {
        Text(
            text = "$$price",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }

    @Composable
    fun TextAddress(address: String) {
        Row {
            Icon(
                imageVector = (Icons.Outlined.PinDrop),
                contentDescription = null // decorative element
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = address, style = MaterialTheme.typography.body2)
        }
    }

    @Composable
    fun PropertyAttributes(surface: Int, rooms: Int, bedRooms: Int, bathRoom: Int) {
        val attributeIndexList = listOf(0, 1, 2, 3)
        val attributeList = listOf("$surface sq m", "$rooms rooms", "$bedRooms bedrooms", "$bathRoom bathrooms")
        val attributeIconList = listOf(Icons.Outlined.Straighten, Icons.Outlined.Home, Icons.Outlined.Bed, Icons.Outlined.Bathroom)
        val contentDescription = listOf("ruler icon", "home icon", "bed icon", "bathroom icon")
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            items(attributeIndexList) { attributeIndex ->
                AttributeItem(icon = attributeIconList[attributeIndex], description = attributeList[attributeIndex], contentDescription = contentDescription[attributeIndex])
            }
        }
    }

    @Composable
    fun AttributeItem(icon: ImageVector, description: String, contentDescription: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = (icon),
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = description, fontSize = 11.sp)
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
            MyApp()
        }
    }

}