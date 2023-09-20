package com.tristaam.todo.ui.dialog.createProject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.model.Project
import kotlinx.coroutines.launch

class CreateProjectViewModel(context: Context):ViewModel() {
    private val projectRepository: ProjectRepository

    init {
        projectRepository = ProjectRepository(context)
    }

    fun insertProject(project: Project) = viewModelScope.launch {
        projectRepository.insertProject(project)
    }

    class CreateProjectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateProjectViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateProjectViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}