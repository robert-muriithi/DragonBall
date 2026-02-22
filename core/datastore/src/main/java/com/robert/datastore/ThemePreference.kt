package com.robert.datastore

enum class ThemePreference {
    LIGHT,
    DARK,
    SYSTEM;

    fun getTheme(): ThemePreference = when (this) {
        LIGHT -> DARK
        DARK -> SYSTEM
        SYSTEM -> LIGHT
    }
}
