package com.tristaam.todo.utils.timePicker

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tristaam.todo.R
import java.time.LocalTime

class TimePickerDialog {
    companion object {
        fun show(fragmentManager: FragmentManager, tag: String, listener: ITimePickerListener) {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(LocalTime.now().hour)
                .setMinute(LocalTime.now().minute)
                .build()
            materialTimePicker.addOnPositiveButtonClickListener {
                listener.onTimeSelected(materialTimePicker.hour, materialTimePicker.minute)
            }
            materialTimePicker.show(fragmentManager, tag)
        }
    }
}