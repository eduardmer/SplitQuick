package com.splitquick.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val darkModeKey = booleanPreferencesKey("dark_mode")
        val languageKey = stringPreferencesKey("language")
    }

    val isDarkModeEnabled: Flow<Boolean> = dataStore.data.map {
        it[Keys.darkModeKey] ?: false
    }

    val language: Flow<String> = dataStore.data.map {
        it[Keys.languageKey] ?: "English"
    }

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.darkModeKey] = darkMode
        }
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[Keys.languageKey] = language
        }
    }

}
