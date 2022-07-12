package com.example.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        RealEstateManagerTheme() {
            ListOfProperty(propertyList = SampleProperties.samplePropertyList)
        }
    }

    @Composable
    fun ListOfProperty(propertyList: List<Property>){
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(propertyList){ property ->
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
        var expended by remember { mutableStateOf(false)}

        Row(
            modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )) {
            Image(
                painter = painterResource(id = R.drawable.propertyplaceholder),
                contentDescription = "Image of the property",
                modifier = Modifier
                    .size(100.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Center) {
                TextPropertyType(propertyType = property.type)
                TextNeighbourhoodAndCity(neighbourhood = property.neighbourhood, city = property.city)
                TextPrice(price = property.price)
            }

            IconButton(onClick = { /*TODO*/ }) {

            }
        }
    }

    @Composable
    fun TextPropertyType(propertyType: String){
        Text(text = propertyType, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
    }

    @Composable
    fun TextNeighbourhoodAndCity(neighbourhood:String, city:String){
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = neighbourhood, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.secondary)
            Text(text = city, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.secondary)
        }
    }

    @Composable
    fun TextPrice(price: Int){
        Text(text = "$$price", style = MaterialTheme.typography.h5, color =  MaterialTheme.colors.primary)
    }

    @Preview(showBackground = true)
    @Composable
    fun FragmentPreview() {
        RealEstateManagerTheme {
            MyApp()
        }
    }

}