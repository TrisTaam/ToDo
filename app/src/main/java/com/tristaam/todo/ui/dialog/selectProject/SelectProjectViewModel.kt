package com.tristaam.todo.ui.dialog.selectProject

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.model.Project

class SelectProjectViewModel(context: Context) : ViewModel() {
    private val projectRepository: ProjectRepository

    init {
        projectRepository = ProjectRepository(context)
    }

    fun getAllProjects(): LiveData<List<Project>> = projectRepository.getAllProjects()

    class SelectProjectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectProjectViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SelectProjectViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}