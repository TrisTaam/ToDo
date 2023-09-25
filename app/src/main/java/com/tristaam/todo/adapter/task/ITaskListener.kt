package com.tristaam.todo.adapter.task

import com.tristaam.todo.model.Task

interface ITaskListener {
    fun onTaskClicked(task: Task)
    fun onTaskTick(task: Task)
}