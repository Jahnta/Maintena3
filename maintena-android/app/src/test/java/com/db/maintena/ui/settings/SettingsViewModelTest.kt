package com.db.maintena.ui.settings

import com.db.maintena.MainDispatcherRule
import com.db.maintena.data.settings.SettingsRepository
import com.db.maintena.data.settings.ThemeMode
import com.db.maintena.data.settings.UserSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiStateReflectsRepositoryAndActionsUpdateIt() =
        runTest(mainDispatcherRule.testDispatcher) {
            val repository = FakeSettingsRepository()
            val viewModel = SettingsViewModel(repository)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect()
            }

            repository.settingsState.value = UserSettings(
                themeMode = ThemeMode.DARK,
                dynamicColorEnabled = false,
            )
            advanceUntilIdle()
            assertEquals(
                SettingsUiState(
                    themeMode = ThemeMode.DARK,
                    dynamicColorEnabled = false,
                ),
                viewModel.uiState.value,
            )

            viewModel.setThemeMode(ThemeMode.LIGHT)
            viewModel.setDynamicColorEnabled(true)
            advanceUntilIdle()

            assertEquals(ThemeMode.LIGHT, repository.settingsState.value.themeMode)
            assertEquals(true, repository.settingsState.value.dynamicColorEnabled)
        }

    private class FakeSettingsRepository : SettingsRepository {
        val settingsState = MutableStateFlow(UserSettings())
        override val settings: Flow<UserSettings> = settingsState

        override suspend fun setThemeMode(themeMode: ThemeMode) {
            settingsState.value = settingsState.value.copy(themeMode = themeMode)
        }

        override suspend fun setDynamicColorEnabled(enabled: Boolean) {
            settingsState.value = settingsState.value.copy(dynamicColorEnabled = enabled)
        }
    }
}
