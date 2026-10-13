package com.example.habittracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = AppSurface,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DarkText,
    secondary = AccentMint,
    onSecondary = AppSurface,
    secondaryContainer = SoftMint,
    onSecondaryContainer = DarkText,
    tertiary = PrimaryBlue,
    onTertiary = AppSurface,
    background = AppBackground,
    onBackground = DarkText,
    surface = AppSurface,
    onSurface = DarkText,
    surfaceVariant = SoftBlue,
    onSurfaceVariant = SecondaryText,
    outline = AppBorder
)

@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
