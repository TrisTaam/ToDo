package com.tristaam.todo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        val channel =
            NotificationChannel("alertChannel", "Alert", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Alert your task"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}