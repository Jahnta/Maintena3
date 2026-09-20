package com.db.maintena.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "user_settings")

@Singleton
class DefaultSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {
    override val settings: Flow<UserSettings> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSettings(
                themeMode = ThemeMode.fromStorageValue(preferences[THEME_MODE]),
                dynamicColorEnabled = preferences[DYNAMIC_COLOR_ENABLED] ?: true,
            )
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode.name
        }
    }

    override suspend fun setDynamicColorEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_ENABLED] = enabled
        }
    }

    private companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR_ENABLED = booleanPreferencesKey("dynamic_color_enabled")
    }
}
