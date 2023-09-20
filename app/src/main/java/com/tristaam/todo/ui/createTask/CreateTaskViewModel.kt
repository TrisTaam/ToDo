package com.tristaam.todo.ui.createTask

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.database.subtask.SubtaskRepository
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.model.Task
import kotlinx.coroutines.launch

class CreateTaskViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    private val subtaskRepository: SubtaskRepository
    var projectId: Int = 0
    private var _subtasks = MutableLiveData<List<Subtask>>()
    private val subtasks get() = _subtasks

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
        subtaskRepository = SubtaskRepository(context)
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
    }

    fun getProject(id: Int): LiveData<Project> = projectRepository.getProject(id)

    fun insertSubtask(subtask: Subtask) = viewModelScope.launch {
        subtaskRepository.insertSubtask(subtask)
    }

    fun updateSubtask(subtask: Subtask) = viewModelScope.launch {
        subtaskRepository.updateSubtask(subtask)
    }

    fun deleteSubtask(subtask: Subtask) = viewModelScope.launch {
        subtaskRepository.deleteSubtask(subtask)
    }

    fun getAllSubtasks(): LiveData<List<Subtask>> = subtaskRepository.getAllSubtasks()

    class CreateTaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateTaskViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}