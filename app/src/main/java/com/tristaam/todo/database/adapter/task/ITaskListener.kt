package com.tristaam.todo.database.adapter.task

import com.tristaam.todo.model.Task

interface ITaskListener {
    fun onTaskClicked(task: Task)
}