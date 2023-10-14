package com.tristaam.todo.utils.timePicker

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime

class TimePickerDialog {
    companion object {
        fun show(
            fragmentManager: FragmentManager,
            tag: String,
            useCurrentTime: Boolean,
            listener: ITimePickerListener
        ) {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(if (useCurrentTime) LocalTime.now().hour else 0)
                .setMinute(if (useCurrentTime) LocalTime.now().minute else 0)
                .build()
            materialTimePicker.addOnPositiveButtonClickListener {
                listener.onTimeSelected(materialTimePicker.hour, materialTimePicker.minute)
            }
            materialTimePicker.show(fragmentManager, tag)
        }
    }
}