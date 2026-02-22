package com.robert.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DragonBallOrange,
    onPrimary = Color.White,
    primaryContainer = DragonBallOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = DragonBallCyan,
    onSecondary = Color.Black,
    secondaryContainer = DragonBallCyanDark,
    onSecondaryContainer = Color.White,
    tertiary = DragonBallYellow,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextGray,
    error = DragonBallRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DragonBallOrange,
    onPrimary = Color.White,
    primaryContainer = DragonBallOrangeLight,
    onPrimaryContainer = Color.Black,
    secondary = DragonBallCyan,
    onSecondary = Color.Black,
    secondaryContainer = DragonBallCyanLight,
    onSecondaryContainer = Color.Black,
    tertiary = DragonBallYellow,
    onTertiary = Color.Black,
    background = LightBackground,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextDarkGray,
    error = DragonBallRed,
    onError = Color.White
)


data class DragonBallExtendedColors(
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val accentOrange: Color,
    val accentCyan: Color,
    val statusAlive: Color,
    val statusDeceased: Color,
    val kiColor: Color,
    val affiliationColor: Color
)

val LocalDragonBallColors = staticCompositionLocalOf {
    DragonBallExtendedColors(
        cardBackground = DarkCard,
        textPrimary = TextWhite,
        textSecondary = TextGray,
        textTertiary = TextLightGray,
        accentOrange = DragonBallOrange,
        accentCyan = DragonBallCyan,
        statusAlive = StatusAlive,
        statusDeceased = StatusDeceased,
        kiColor = DragonBallOrange,
        affiliationColor = DragonBallCyan
    )
}

private val DarkExtendedColors = DragonBallExtendedColors(
    cardBackground = DarkCard,
    textPrimary = TextWhite,
    textSecondary = TextGray,
    textTertiary = TextLightGray,
    accentOrange = DragonBallOrange,
    accentCyan = DragonBallCyan,
    statusAlive = StatusAlive,
    statusDeceased = StatusDeceased,
    kiColor = DragonBallOrange,
    affiliationColor = DragonBallCyan
)

private val LightExtendedColors = DragonBallExtendedColors(
    cardBackground = LightSurface,
    textPrimary = Color.Black,
    textSecondary = TextDarkGray,
    textTertiary = TextGray,
    accentOrange = DragonBallOrange,
    accentCyan = DragonBallCyanDark,
    statusAlive = StatusAlive,
    statusDeceased = StatusDeceased,
    kiColor = DragonBallOrange,
    affiliationColor = DragonBallCyanDark
)

@Composable
fun DragonBallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalDragonBallColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = DragonBallTypography,
            shapes = DragonBallShapes,
            content = content
        )
    }
}


object DragonBallTheme {
    val colors: DragonBallExtendedColors
        @Composable
        get() = LocalDragonBallColors.current
}

