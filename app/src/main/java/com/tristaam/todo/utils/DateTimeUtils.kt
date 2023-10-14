package com.tristaam.todo.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun Date.minus(other: Date): Date {
        val calendarThis = Calendar.getInstance()
        calendarThis.time = this
        val calendarOther = Calendar.getInstance()
        calendarOther.time = other
        calendarThis.add(Calendar.HOUR_OF_DAY, -calendarOther.get(Calendar.HOUR_OF_DAY))
        calendarThis.add(Calendar.MINUTE, -calendarOther.get(Calendar.MINUTE))
        return calendarThis.time
    }

    fun Date.getHour(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun Date.getMinute(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.MINUTE)
    }
}