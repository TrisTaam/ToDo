package com.tristaam.todo.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtils {
    val inputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
}