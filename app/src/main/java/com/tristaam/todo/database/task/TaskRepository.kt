package com.tristaam.todo.database.task

import android.content.Context
import androidx.lifecycle.LiveData
import com.tristaam.todo.model.Task
import com.tristaam.todo.database.TodoDatabase

class TaskRepository(context: Context) {
    private val taskDao: TaskDao

    init {
        taskDao = TodoDatabase.getInstance(context).taskDao()
    }

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()
    fun getTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskDao.getTasksByProjectId(projectId)
}