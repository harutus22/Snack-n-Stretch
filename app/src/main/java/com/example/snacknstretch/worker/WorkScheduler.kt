package com.example.snacknstretch.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.snacknstretch.model.MainUiState
import java.util.concurrent.TimeUnit

class WorkScheduler(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    companion object {
        const val PERIODIC_WORK = "periodic_work"
        const val DATA_WORKING_TIME = "working_time"
        const val DATA_BREAK_TIME = "break_time"
        const val DATA_IS_BATTERY_LOW = "is_battery_low"
        const val DATA_IS_CHARGING = "is_charging"
        const val DATA_IS_WORK = "is_work"
    }

    fun startPeriodicSync(data: MainUiState) {
        val isBatteryLowSwitched = data.isBatteryLowSwitched
        val isChargingSwitched = data.isBatteryLowSwitched
        val breakTime = data.breakTime.time.toLong()
        val workTime = data.workingTime.time.toLong()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(isBatteryLowSwitched)
            .setRequiresCharging(isChargingSwitched)
            .build()

        val breakRequest = OneTimeWorkRequestBuilder<NotificationWorker>(
        ).setInitialDelay(workTime, TimeUnit.MINUTES).setConstraints(constraints)
            .addTag(PERIODIC_WORK)
            .setInputData(
                workDataOf(
                    DATA_IS_BATTERY_LOW to isBatteryLowSwitched,
                    DATA_IS_CHARGING to isChargingSwitched,
                    DATA_BREAK_TIME to breakTime,
                    DATA_WORKING_TIME to workTime,
                    DATA_IS_WORK to true,
                )
            )
            .build()

        workManager.enqueueUniqueWork(
            PERIODIC_WORK,
            ExistingWorkPolicy.REPLACE,
            breakRequest
        )
    }

    fun stopPeriodicSync() {
        workManager.cancelAllWorkByTag(PERIODIC_WORK)
    }
}