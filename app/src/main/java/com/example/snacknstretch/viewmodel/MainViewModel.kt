package com.example.snacknstretch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.snacknstretch.R
import com.example.snacknstretch.data.ScheduledDataRepository
import com.example.snacknstretch.model.MainUiState
import com.example.snacknstretch.worker.WorkScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val repository: ScheduledDataRepository
) : AndroidViewModel(application) {
    private val workScheduler = WorkScheduler(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val newValue = repository.getSavedState()
            _uiState.value = newValue.copy(isLoading = false)
        }
    }

    fun updateState(newState: MainUiState) {
        _uiState.value = newState
    }

    fun startWork() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                text = application.getString(
                    R.string.session_scheduled_m_work_m_break,
                    _uiState.value.workingTime.time,
                    _uiState.value.breakTime.time
                )
            )
            workScheduler.startPeriodicSync(_uiState.value)
            repository.saveState(_uiState.value)
        }
    }

    fun stopWork() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                text = ""
            )
            workScheduler.stopPeriodicSync()
            repository.clearPreferences()
        }
    }
}