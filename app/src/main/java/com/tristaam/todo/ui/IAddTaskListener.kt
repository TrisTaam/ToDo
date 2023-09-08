package com.tristaam.todo.ui

import com.tristaam.todo.model.Task

interface IAddTaskListener {
    fun addTask(task: Task)
}