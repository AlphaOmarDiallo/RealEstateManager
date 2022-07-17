package com.example.realestatemanager.domain

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SharedComposable {
    @Composable
    fun PropertyAttributes(surface: Int, rooms: Int, bedRooms: Int, bathRoom: Int) {
        val attributeIndexList = listOf(0, 1, 2, 3)
        val attributeList =
            listOf("$surface sq m", "$rooms rooms", "$bedRooms bedrooms", "$bathRoom bathrooms")
        val attributeIconList = listOf(
            Icons.Outlined.Straighten,
            Icons.Outlined.Home,
            Icons.Outlined.Bed,
            Icons.Outlined.Bathroom
        )
        val contentDescription = listOf("ruler icon", "home icon", "bed icon", "bathroom icon")
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            items(attributeIndexList) { attributeIndex ->
                AttributeItem(
                    icon = attributeIconList[attributeIndex],
                    description = attributeList[attributeIndex],
                    contentDescription = contentDescription[attributeIndex]
                )
            }
        }
    }

    @Composable
    fun AttributeItem(icon: ImageVector, description: String, contentDescription: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = (icon),
                contentDescription = contentDescription,
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = description, fontSize = 11.sp)
        }
    }

    @Composable
    fun TextPrice(price: Int, style: TextStyle = MaterialTheme.typography.h5, color: Color = MaterialTheme.colors.primary) {
        Text(
            text = "$$price",
            style = style,
            color = color
        )
    }

    @Composable
    fun TextPropertyType(propertyType: String, style: TextStyle, fontWeight: FontWeight, color: Color) {
        Text(
            text = propertyType,
            style = style,
            fontWeight = fontWeight,
            color = color
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
    fun CardSmall() {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            border = BorderStroke(2.dp, color = MaterialTheme.colors.secondary)
        ) {
            
        }
    }
}