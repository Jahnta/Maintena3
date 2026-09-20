package com.db.maintena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.db.maintena.data.settings.ThemeMode
import com.db.maintena.ui.settings.SettingsScreen
import com.db.maintena.ui.settings.SettingsUiState
import com.db.maintena.ui.settings.SettingsViewModel
import com.db.maintena.ui.settings.ThemeSelectionScreen
import com.db.maintena.ui.theme.MaintenaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            MaintenaTheme(
                themeMode = settingsUiState.themeMode,
                dynamicColor = settingsUiState.dynamicColorEnabled,
            ) {
                MaintenaApp(
                    settingsUiState = settingsUiState,
                    onThemeModeChange = settingsViewModel::setThemeMode,
                    onDynamicColorEnabledChange = settingsViewModel::setDynamicColorEnabled,
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MaintenaApp(
    settingsUiState: SettingsUiState = SettingsUiState(),
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onDynamicColorEnabledChange: (Boolean) -> Unit = {},
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestination.HOME) }
    val homeBackStack = rememberNavBackStack(HomeRoute)
    val favoritesBackStack = rememberNavBackStack(FavoritesRoute)
    val profileBackStack = rememberNavBackStack(ProfileRoute)
    val settingsBackStack = rememberNavBackStack(SettingsRoute)
    val currentBackStack = when (currentDestination) {
        AppDestination.HOME -> homeBackStack
        AppDestination.FAVORITES -> favoritesBackStack
        AppDestination.PROFILE -> profileBackStack
        AppDestination.SETTINGS -> settingsBackStack
    }

    BackHandler(
        enabled = currentDestination != AppDestination.HOME && currentBackStack.size == 1,
    ) {
        currentDestination = AppDestination.HOME
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.iconRes),
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.labelRes)) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination },
                )
            }
        },
    ) {
        NavDisplay(
            backStack = currentBackStack,
            onBack = { currentBackStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    PlaceholderScreen(destination = AppDestination.HOME)
                }
                entry<FavoritesRoute> {
                    PlaceholderScreen(destination = AppDestination.FAVORITES)
                }
                entry<ProfileRoute> {
                    PlaceholderScreen(destination = AppDestination.PROFILE)
                }
                entry<SettingsRoute> {
                    SettingsScreen(
                        uiState = settingsUiState,
                        onThemeClick = {
                            if (settingsBackStack.lastOrNull() != ThemeSettingsRoute) {
                                settingsBackStack.add(ThemeSettingsRoute)
                            }
                        },
                        onDynamicColorEnabledChange = onDynamicColorEnabledChange,
                    )
                }
                entry<ThemeSettingsRoute> {
                    ThemeSelectionScreen(
                        selectedThemeMode = settingsUiState.themeMode,
                        onThemeModeChange = onThemeModeChange,
                        onBack = { settingsBackStack.removeLastOrNull() },
                    )
                }
            },
        )
    }
}

@Serializable
private data object HomeRoute : NavKey

@Serializable
private data object FavoritesRoute : NavKey

@Serializable
private data object ProfileRoute : NavKey

@Serializable
private data object SettingsRoute : NavKey

@Serializable
private data object ThemeSettingsRoute : NavKey

private enum class AppDestination(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
) {
    HOME(R.string.nav_home, R.drawable.ic_home),
    FAVORITES(R.string.nav_favorites, R.drawable.ic_favorite),
    PROFILE(R.string.nav_profile, R.drawable.ic_account_box),
    SETTINGS(R.string.nav_settings, R.drawable.ic_settings),
}

@Composable
private fun PlaceholderScreen(
    destination: AppDestination,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = stringResource(
                R.string.placeholder_screen,
                stringResource(destination.labelRes),
            ),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
