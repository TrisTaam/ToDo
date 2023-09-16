package com.tristaam.todo.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.model.Project
import kotlinx.coroutines.launch

class ProjectViewModel(context: Context) : ViewModel() {
    private val projectRepository: ProjectRepository

    init {
        projectRepository = ProjectRepository(context)
    }

    fun insertProject(project: Project) = viewModelScope.launch {
        projectRepository.insertProject(project)
    }

    fun updateProject(project: Project) = viewModelScope.launch {
        projectRepository.updateProject(project)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {
        projectRepository.deleteProject(project)
    }

    fun getAllProjects(): LiveData<List<Project>> = projectRepository.getAllProjects()

    class ProjectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProjectViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}