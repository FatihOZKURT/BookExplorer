package com.example.bookexplorer.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object ThemePreferenceKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

class ThemePreference(private val context: Context) {

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE] = enabled
        }
    }
}

