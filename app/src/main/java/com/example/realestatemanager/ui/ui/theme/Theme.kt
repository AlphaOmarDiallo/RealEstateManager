package com.example.realestatemanager.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryDarkColor,
    onPrimary = darkTextColor,
    onSecondary = secondaryTextColor,
    background = primaryColor
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = secondaryLightColor,
    secondary = primaryDarkColor,
    secondaryVariant = secondaryLightColor,
    onPrimary = primaryTextColor,
    onSecondary = darkTextColor,
    background = lightBackground

    /*
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,*/

)

@Composable
fun RealEstateManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}