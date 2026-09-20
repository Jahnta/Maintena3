package com.db.maintena.data.settings

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM;

    companion object {
        internal fun fromStorageValue(value: String?): ThemeMode =
            entries.firstOrNull { it.name == value } ?: SYSTEM
    }
}
