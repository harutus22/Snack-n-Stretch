package com.example.snacknstretch.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.snacknstretch.data.ScheduledDataRepository

class MainViewModelFactory(
    private val application: Application,
    private val repository: ScheduledDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                application,
                repository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}