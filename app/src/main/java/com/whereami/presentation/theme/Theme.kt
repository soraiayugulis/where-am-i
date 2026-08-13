package com.whereami.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val lightColors = lightColorScheme(
    primary = OceanBlue,
    onPrimary = White,
    primaryContainer = SkyBase,
    onPrimaryContainer = White,
    secondary = AccentYellow,
    onSecondary = White,
    tertiary = EarthGreen,
    background = SkyBase,
    onBackground = White,
    surface = SurfaceBlue,
    onSurface = DarkBlue,
    error = PinRed,
    onError = White
)

private val darkColors = darkColorScheme(
    primary = OceanBlue,
    onPrimary = White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = White,
    secondary = AccentYellow,
    onSecondary = White,
    tertiary = EarthGreen,
    background = DarkBlue,
    onBackground = White,
    surface = SoftBlue,
    onSurface = White,
    error = PinRed,
    onError = White
)

@Composable
fun WhereAmITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColors
        else -> lightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
