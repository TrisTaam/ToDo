package com.tristaam.todo.database.subtask

import android.content.Context
import com.tristaam.todo.database.TodoDatabase
import com.tristaam.todo.model.Subtask

class SubtaskRepository(val context: Context) {
    private val subtaskDao: SubtaskDao

    init {
        subtaskDao = TodoDatabase.getInstance(context).subtaskDao()
    }

    suspend fun insertSubtask(subtask: Subtask) = subtaskDao.insertSubtask(subtask)
    suspend fun updateSubtask(subtask: Subtask) = subtaskDao.updateSubtask(subtask)
    suspend fun deleteSubtask(subtask: Subtask) = subtaskDao.deleteSubtask(subtask)
    fun getAllSubtasks() = subtaskDao.getAllSubtasks()
}