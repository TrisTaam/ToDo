package com.tristaam.todo.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tristaam.todo.utils.Notification

class AlarmWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Notification.createNotification(context, "Hi", "Hi")
        return Result.success()
    }
}