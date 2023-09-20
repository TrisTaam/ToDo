package com.tristaam.todo.ui.dialog.selectPriority

import com.tristaam.todo.model.Priority

interface ISelectPriorityListener {
    fun onSelectPriority(priority: Priority)
}