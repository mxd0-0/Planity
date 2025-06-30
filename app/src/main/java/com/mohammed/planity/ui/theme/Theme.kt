package com.mohammed.planity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PlanityPurple,
    onPrimary = Color.White,
    secondary = ChipYellow,
    onSecondary = ChipYellowText,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface, // Used for Bottom Navigation Bar
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant, // Used for SearchBar, Chips
    onSurfaceVariant = DarkOnSurfaceVariant // Gray text color
)

@Composable
fun PlanityTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography, // Assumes you have a Type.kt file
        content = content
    )
}