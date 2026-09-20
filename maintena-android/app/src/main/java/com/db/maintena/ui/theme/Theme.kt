package com.db.maintena.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.db.maintena.data.settings.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = StaticDarkPrimary,
    secondary = StaticDarkSecondary,
    tertiary = StaticDarkTertiary,
)

private val LightColorScheme = lightColorScheme(
    primary = StaticLightPrimary,
    secondary = StaticLightSecondary,
    tertiary = StaticLightTertiary,
    surface = Color.White,
)

@Composable
fun MaintenaTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    val appColorScheme = colorScheme.copy(
        background = if (darkTheme) colorScheme.surface else colorScheme.surfaceContainerLow,
        surface = if (darkTheme) colorScheme.surfaceContainerLow else Color.White,
    )

    MaterialTheme(
        colorScheme = appColorScheme,
        typography = MaintenaTypography,
        shapes = MaintenaShapes,
        content = content
    )
}
