package com.example.snacknstretch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.snacknstretch.R
import com.example.snacknstretch.model.BreakTime
import com.example.snacknstretch.model.MainUiState
import com.example.snacknstretch.model.WorkingTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ScheduledDataRepository(
    private val dataStore: DataStore<Preferences>,
    private val context: Context
) {
    private val workTime = intPreferencesKey(context.getString(R.string.key_work_time))
    private val breakTime = intPreferencesKey(context.getString(R.string.key_break_time))
    private val isCharging = booleanPreferencesKey(context.getString(R.string.key_is_charging))
    private val isBatteryLow = booleanPreferencesKey(context.getString(R.string.key_is_battery_low))

    val state: Flow<MainUiState> =
        dataStore.data.map { preferences ->
            val workData = WorkingTime.entries[preferences[workTime] ?: 0]
            val breakData = BreakTime.entries[preferences[breakTime] ?: 0]
            MainUiState(
                workingTime = workData,
                breakTime = breakData,
                text = if (preferences[workTime] == null ) "" else context.getString(
                    R.string.session_scheduled_m_work_m_break,
                    workData.time,
                    breakData.time
                ),
                isChargingSwitched = preferences[isCharging] ?: false,
                isBatteryLowSwitched = preferences[isBatteryLow] ?: false,
            )
        }

    suspend fun getSavedState(): MainUiState {
        return dataStore.data.map { preferences ->
            val workData = WorkingTime.entries[preferences[workTime] ?: 0]
            val breakData = BreakTime.entries[preferences[breakTime] ?: 0]
            MainUiState(
                workingTime = workData,
                breakTime = breakData,
                text = if (preferences[workTime] == null) "" else context.getString(
                    R.string.session_scheduled_m_work_m_break,
                    workData.time,
                    breakData.time
                ),
                isChargingSwitched = preferences[isCharging] ?: false,
                isBatteryLowSwitched = preferences[isBatteryLow] ?: false,
            )
        }.first()
    }

    suspend fun saveState(state: MainUiState) {
        dataStore.edit { preferences ->
            preferences[workTime] = state.workingTime.ordinal
            preferences[breakTime] = state.breakTime.ordinal
            preferences[isCharging] = state.isChargingSwitched
            preferences[isBatteryLow] = state.isBatteryLowSwitched
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}