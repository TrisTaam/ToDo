package com.tristaam.todo.database.task

import android.content.Context
import androidx.lifecycle.LiveData
import com.tristaam.todo.database.TodoDatabase
import com.tristaam.todo.model.Task
import java.util.Date

class TaskRepository(context: Context) {
    private val taskDao: TaskDao

    init {
        taskDao = TodoDatabase.getInstance(context).taskDao()
    }

    suspend fun insertTask(task: Task): Int = taskDao.insertTask(task).toInt()
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()
    fun getTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskDao.getTasksByProjectId(projectId)

    fun getTaskById(taskId: Int): LiveData<Task> = taskDao.getTaskById(taskId)
    fun getCompletedTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskDao.getCompletedTasksByProjectId(projectId)

    fun getUncompletedTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskDao.getUncompletedTasksByProjectId(projectId)

    fun getAllUncompletedTasks(): LiveData<List<Task>> = taskDao.getAllUncompletedTasks()
    fun getTasksCountByProjectId(projectId: Int): LiveData<Int> =
        taskDao.getTasksCountByProjectId(projectId)

    fun getAllTasksCount(): LiveData<Int> = taskDao.getAllTasksCount()

    fun getCompletedTasksCountByProjectId(projectId: Int): LiveData<Int> =
        taskDao.getCompletedTasksCountByProjectId(projectId)

    fun getAllCompletedTasksCount(): LiveData<Int> = taskDao.getAllCompletedTasksCount()

    fun getAllCompletedTasks(): LiveData<List<Task>> = taskDao.getAllCompletedTasks()

    fun getTasksByDate(startDate: Date, endDate: Date): LiveData<List<Task>> =
        taskDao.getTasksByDate(startDate, endDate)
}