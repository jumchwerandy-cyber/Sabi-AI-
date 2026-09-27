package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SabiDarkColorScheme = darkColorScheme(
    primary = SabiGreenPrimary,
    onPrimary = Color.Black,
    primaryContainer = SabiGreenContainer,
    onPrimaryContainer = SabiGreenGlow,
    secondary = SabiGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = SabiBlue,
    onTertiary = Color.Black,
    background = SabiNavyDark,
    onBackground = SabiTextPrimary,
    surface = SabiNavySurface,
    onSurface = SabiTextPrimary,
    surfaceVariant = SabiNavyElevated,
    onSurfaceVariant = SabiTextSecondary,
    outline = SabiNavyBorder,
    error = SabiError,
    onError = Color.White
)

private val SabiLightColorScheme = lightColorScheme(
    primary = SabiGreenDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = SabiGreenDark,
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Color(0xFF0284C7),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = SabiError,
    onError = Color.White
)

@Composable
fun SabiAiTheme(
    darkTheme: Boolean = true, // Default to SABI tech dark aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SabiDarkColorScheme else SabiLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
