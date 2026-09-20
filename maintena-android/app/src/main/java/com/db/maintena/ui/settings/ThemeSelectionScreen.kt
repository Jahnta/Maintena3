package com.db.maintena.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.db.maintena.R
import com.db.maintena.data.settings.ThemeMode
import com.db.maintena.ui.theme.MaintenaDimensions
import com.db.maintena.ui.theme.MaintenaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionScreen(
    selectedThemeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_theme_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.settings_back),
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        SettingsContentList(
            modifier = Modifier
                .padding(innerPadding),
            topPadding = MaintenaDimensions.compactScreenPadding,
        ) {
            item {
                SettingsGroup(
                    modifier = Modifier
                        .widthIn(max = MaintenaDimensions.contentMaxWidth)
                        .fillMaxWidth(),
                ) {
                    themeOptions.forEach { option ->
                        SettingsSingleChoiceItem(
                            title = stringResource(option.labelRes),
                            selected = selectedThemeMode == option.mode,
                            onClick = { onThemeModeChange(option.mode) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSelectionScreenPreview() {
    MaintenaTheme(themeMode = ThemeMode.LIGHT, dynamicColor = false) {
        ThemeSelectionScreen(
            selectedThemeMode = ThemeMode.SYSTEM,
            onThemeModeChange = {},
            onBack = {},
        )
    }
}
