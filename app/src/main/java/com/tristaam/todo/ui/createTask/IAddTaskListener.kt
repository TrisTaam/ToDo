package com.tristaam.todo.ui.createTask

import android.os.Parcelable
import com.tristaam.todo.model.Task
import java.io.Serializable

interface IAddTaskListener : Serializable {
    fun onAddTask(task: Task)
}