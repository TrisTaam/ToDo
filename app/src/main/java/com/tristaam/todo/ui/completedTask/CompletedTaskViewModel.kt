package com.tristaam.todo.ui.completedTask

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompletedTaskViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    var projectId: Int = 0

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

    private fun getCompletedTasksByProjectId(projectId: Int) =
        taskRepository.getCompletedTasksByProjectId(projectId)

    private fun getAllCompletedTasks() = taskRepository.getAllCompletedTasks()

    fun getTasks(): LiveData<List<Task>> {
        return if (projectId == 0) {
            getAllCompletedTasks()
        } else {
            getCompletedTasksByProjectId(projectId)
        }
    }

    fun getProject(projectId: Int) = projectRepository.getProject(projectId)

    class CompletedTaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CompletedTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CompletedTaskViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}