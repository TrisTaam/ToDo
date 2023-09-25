package com.tristaam.todo.utils.datePicker

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class DatePickerDialog {
    companion object {
        fun show(fragmentManager: FragmentManager, tag: String, listener: IDatePickerListener) {
            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            materialDatePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                listener.onDateSelected(day, month, year)
            }
            materialDatePicker.show(fragmentManager, tag)
        }
    }
}