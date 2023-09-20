package com.tristaam.todo.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtils {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val inputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
}