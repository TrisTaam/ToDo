package com.tristaam.todo.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.tristaam.todo.R
import com.tristaam.todo.work.AlarmWorker
import java.util.Date
import java.util.concurrent.TimeUnit

object Notification {
    fun createNotification(context: Context, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, "alertChannel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(getNotificationId(), notification)
    }

    fun createWorkRequest(context: Context, date: Date) {
        val workRequest: WorkRequest =
            OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInitialDelay(
                    date.time - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun getNotificationId(): Int {
        return Date().time.toInt()
    }
}