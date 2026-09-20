package com.db.maintena.data.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {
    @Test
    fun fromStorageValue_returnsStoredMode() {
        assertEquals(ThemeMode.LIGHT, ThemeMode.fromStorageValue("LIGHT"))
        assertEquals(ThemeMode.DARK, ThemeMode.fromStorageValue("DARK"))
    }

    @Test
    fun fromStorageValue_fallsBackToSystemForMissingOrUnknownValue() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue(null))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue("UNSUPPORTED"))
    }
}
