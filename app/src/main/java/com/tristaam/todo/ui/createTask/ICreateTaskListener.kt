package com.tristaam.todo.ui.createTask

import com.tristaam.todo.model.Task
import java.io.Serializable

interface ICreateTaskListener : Serializable {
    fun onCreateTask(task: Task)
}