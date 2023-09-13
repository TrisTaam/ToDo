package com.tristaam.todo.ui

import com.tristaam.todo.model.Task

interface ITaskListener {
    fun detailTask(task: Task)
}