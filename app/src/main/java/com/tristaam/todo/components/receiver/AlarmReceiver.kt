package com.tristaam.todo.components.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tristaam.todo.utils.Notification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra("bundle")
        val title = bundle?.getString("title")
        val message = bundle?.getString("message")
        Notification.createNotification(context!!, title!!, message!!)
    }
}