package com.db.maintena.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setDynamicColorEnabled(enabled: Boolean)
}
