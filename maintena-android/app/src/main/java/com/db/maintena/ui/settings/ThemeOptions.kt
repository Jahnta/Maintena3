package com.db.maintena.ui.settings

import androidx.annotation.StringRes
import com.db.maintena.R
import com.db.maintena.data.settings.ThemeMode

internal data class ThemeOption(
    val mode: ThemeMode,
    @StringRes val labelRes: Int,
)

internal val themeOptions = listOf(
    ThemeOption(ThemeMode.LIGHT, R.string.settings_theme_light),
    ThemeOption(ThemeMode.DARK, R.string.settings_theme_dark),
    ThemeOption(ThemeMode.SYSTEM, R.string.settings_theme_system),
)

@StringRes
internal fun ThemeMode.labelRes(): Int = when (this) {
    ThemeMode.LIGHT -> R.string.settings_theme_light
    ThemeMode.DARK -> R.string.settings_theme_dark
    ThemeMode.SYSTEM -> R.string.settings_theme_system
}
