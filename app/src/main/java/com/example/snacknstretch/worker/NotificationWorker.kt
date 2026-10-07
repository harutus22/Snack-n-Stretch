package com.example.snacknstretch.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.snacknstretch.R
import java.util.concurrent.TimeUnit

private const val DEFAULT_BREAK_TIME = 5L
private const val DEFAULT_WORK_TIME = 15L

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val isWork = inputData.getBoolean(WorkScheduler.DATA_IS_WORK, false)

        val notification = NotificationCreator()
        notification.createNotificationChannel(context = applicationContext)
        notification.showBreakNotification(applicationContext, applicationContext.getString(if (isWork) R.string.time_for_a_break else R.string.break_over))

        createWorkSchedule(isWork)
        return Result.success()
    }

    private fun createWorkSchedule(isWork: Boolean) {
        val isBatteryLowSwitched = inputData.getBoolean(WorkScheduler.DATA_IS_BATTERY_LOW, false)
        val isChargingSwitched = inputData.getBoolean(WorkScheduler.DATA_IS_CHARGING, false)
        val breakTime = inputData.getLong(WorkScheduler.DATA_BREAK_TIME, DEFAULT_BREAK_TIME)
        val workTime = inputData.getLong(WorkScheduler.DATA_WORKING_TIME, DEFAULT_WORK_TIME)

        val workManager = WorkManager.getInstance(applicationContext)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(isBatteryLowSwitched)
            .setRequiresCharging(isChargingSwitched)
            .build()

        val breakRequest = OneTimeWorkRequestBuilder<NotificationWorker>(
        ).setInitialDelay(if (isWork) breakTime else workTime, TimeUnit.MINUTES).setConstraints(constraints)
            .addTag(WorkScheduler.PERIODIC_WORK)
            .setInputData(
                workDataOf(
                    WorkScheduler.DATA_IS_BATTERY_LOW to isBatteryLowSwitched,
                    WorkScheduler.DATA_IS_CHARGING to isChargingSwitched,
                    WorkScheduler.DATA_BREAK_TIME to breakTime,
                    WorkScheduler.DATA_WORKING_TIME to workTime,
                    WorkScheduler.DATA_IS_WORK to !isWork,
                )
            )
            .build()

        workManager.enqueueUniqueWork(
            WorkScheduler.PERIODIC_WORK,
            ExistingWorkPolicy.REPLACE,
            breakRequest
        )
    }
}