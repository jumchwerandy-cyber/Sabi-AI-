package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.sabiDataStore: DataStore<Preferences> by preferencesDataStore(name = "sabi_user_preferences")

class SabiPreferencesDataStore(private val context: Context) {

    companion object {
        val KEY_SELECTED_LANGUAGE = stringPreferencesKey("sabi_selected_language")
        val KEY_SELECTED_MODE = stringPreferencesKey("sabi_selected_mode")
        val KEY_ACTIVE_MODEL = stringPreferencesKey("sabi_active_model")
        val KEY_LAST_CONVERSATION_ID = stringPreferencesKey("sabi_last_conversation_id")
        val KEY_CACHE_SUMMARY = stringPreferencesKey("sabi_cache_summary")
    }

    private val dataStore = context.sabiDataStore

    val selectedLanguage: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_SELECTED_LANGUAGE] ?: "pidgin"
        }

    val selectedMode: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_SELECTED_MODE] ?: "casual"
        }

    val activeModelId: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_ACTIVE_MODEL] ?: "sabi_v1"
        }

    val lastConversationId: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_LAST_CONVERSATION_ID]
        }

    suspend fun saveSelectedLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[KEY_SELECTED_LANGUAGE] = language
        }
    }

    suspend fun saveSelectedMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[KEY_SELECTED_MODE] = mode
        }
    }

    suspend fun saveActiveModel(modelId: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ACTIVE_MODEL] = modelId
        }
    }

    suspend fun saveLastConversationId(conversationId: String) {
        dataStore.edit { preferences ->
            preferences[KEY_LAST_CONVERSATION_ID] = conversationId
        }
    }

    suspend fun saveCacheSummary(summary: String) {
        dataStore.edit { preferences ->
            preferences[KEY_CACHE_SUMMARY] = summary
        }
    }
}
