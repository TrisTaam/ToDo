package com.tristaam.todo.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.tristaam.todo.R
import java.util.Date

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

    private fun getNotificationId(): Int {
        return Date().time.toInt()
    }
}