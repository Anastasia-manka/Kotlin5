package com.example.kt5_4.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDataStore(private val context: Context) {

    companion object {
        private val COMPLETED_TASK_COLOR_KEY = booleanPreferencesKey("completed_task_color")
    }

    // Поток для отслеживания цвета выполненных задач
    val isDarkForCompletedTasks: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[COMPLETED_TASK_COLOR_KEY] ?: false
        }

    // Сохранение настройки цвета
    suspend fun setCompletedTaskColor(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COMPLETED_TASK_COLOR_KEY] = isDark
        }
    }
}