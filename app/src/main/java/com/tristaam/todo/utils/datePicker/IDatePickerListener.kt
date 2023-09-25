package com.tristaam.todo.utils.datePicker

interface IDatePickerListener {
    fun onDateSelected(day: Int, month: Int, year: Int)
}