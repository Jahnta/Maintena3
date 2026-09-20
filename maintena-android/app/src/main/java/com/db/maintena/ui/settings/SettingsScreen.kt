package com.db.maintena.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.db.maintena.R
import com.db.maintena.data.settings.ThemeMode
import com.db.maintena.ui.theme.MaintenaDimensions
import com.db.maintena.ui.theme.MaintenaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeClick: () -> Unit,
    onDynamicColorEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    dynamicColorAvailable: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        SettingsContentList(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .widthIn(max = MaintenaDimensions.contentMaxWidth)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaintenaDimensions.sectionSpacing),
                ) {
                    SettingsSectionLabel(
                        text = stringResource(R.string.settings_appearance_section),
                    )

                    AppearanceSettingsGroup(
                        themeMode = uiState.themeMode,
                        dynamicColorEnabled = uiState.dynamicColorEnabled,
                        dynamicColorAvailable = dynamicColorAvailable,
                        onThemeClick = onThemeClick,
                        onDynamicColorEnabledChange = onDynamicColorEnabledChange,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppearanceSettingsGroup(
    themeMode: ThemeMode,
    dynamicColorEnabled: Boolean,
    dynamicColorAvailable: Boolean,
    onThemeClick: () -> Unit,
    onDynamicColorEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsGroup(modifier = modifier) {
        SettingsNavigationItem(
            title = stringResource(R.string.settings_theme_title),
            supportingText = stringResource(themeMode.labelRes()),
            onClick = onThemeClick,
        )

        SettingsSwitchItem(
            title = stringResource(R.string.settings_dynamic_color_title),
            checked = dynamicColorEnabled && dynamicColorAvailable,
            enabled = dynamicColorAvailable,
            onCheckedChange = onDynamicColorEnabledChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaintenaTheme(themeMode = ThemeMode.LIGHT, dynamicColor = false) {
        SettingsScreen(
            uiState = SettingsUiState(),
            onThemeClick = {},
            onDynamicColorEnabledChange = {},
        )
    }
}
