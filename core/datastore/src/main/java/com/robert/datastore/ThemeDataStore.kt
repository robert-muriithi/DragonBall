package com.robert.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val themeKey = intPreferencesKey("theme_preference")

    val themePreference: Flow<ThemePreference> = dataStore.data.map { prefs ->
        when (prefs[themeKey]) {
            0 -> ThemePreference.LIGHT
            1 -> ThemePreference.DARK
            else -> ThemePreference.SYSTEM
        }
    }

    suspend fun setTheme(theme: ThemePreference) {
        dataStore.edit { prefs ->
            prefs[themeKey] = when (theme) {
                ThemePreference.LIGHT -> 0
                ThemePreference.DARK -> 1
                ThemePreference.SYSTEM -> 2
            }
        }
    }
}
