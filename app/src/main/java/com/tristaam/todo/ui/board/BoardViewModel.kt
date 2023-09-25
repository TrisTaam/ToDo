package com.tristaam.todo.ui.board

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Task
import kotlinx.coroutines.launch

class BoardViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun getAllTasks(): LiveData<List<Task>> = taskRepository.getAllTasks()

    fun getAllProjects(): LiveData<List<Project>> = projectRepository.getAllProjects()

    fun getTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskRepository.getTasksByProjectId(projectId)

    class BoardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BoardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BoardViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}