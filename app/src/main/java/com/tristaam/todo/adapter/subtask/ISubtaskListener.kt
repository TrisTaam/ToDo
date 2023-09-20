package com.tristaam.todo.adapter.subtask

import com.tristaam.todo.model.Subtask

interface ISubtaskListener {
    fun onTick(subtask: Subtask, isChecked: Boolean)
    fun onDelete(subtask: Subtask)
}