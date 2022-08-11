package com.example.realestatemanager.domain

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

val smallPadding = 4.dp
val mediumPadding = 8.dp
val largePadding = 16.dp

val colorGradient = listOf(
    Color.Transparent,
    Color.Black
)

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
fun TextPrice(
    price: Int,
    euro: Boolean,
    dollarToEuroRate: Double,
    style: TextStyle = MaterialTheme.typography.h5,
    color: Color = MaterialTheme.colors.primary
) {
    val price1 = if (euro) (price * dollarToEuroRate).toInt() else price
    Text(
        text = if (euro) "€$price1" else "$$price1",
        style = style,
        color = color
    )
}

@Composable
fun TextPropertyType(
    propertyType: String,
    style: TextStyle,
    fontWeight: FontWeight,
    color: Color
) {
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
fun TextAddress(
    address: String,
    style: TextStyle = MaterialTheme.typography.body2,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Row {
        Icon(
            imageVector = (Icons.Outlined.PinDrop),
            contentDescription = null // decorative element
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = address, style = style, fontWeight = fontWeight)
    }
}

@Composable
fun SmallTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primaryVariant,
        modifier = modifier
    )
}
