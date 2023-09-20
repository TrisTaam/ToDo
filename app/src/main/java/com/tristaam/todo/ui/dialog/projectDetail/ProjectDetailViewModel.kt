package com.tristaam.todo.ui.dialog.projectDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.model.Project
import kotlinx.coroutines.launch

class ProjectDetailViewModel(context: Context) : ViewModel() {
    private val projectRepository: ProjectRepository

    init {
        projectRepository = ProjectRepository(context)
    }

    fun updateProject(project: Project) = viewModelScope.launch {
        projectRepository.updateProject(project)
    }

    fun getProject(id: Int) = projectRepository.getProject(id)

    fun deleteProject(project: Project) = viewModelScope.launch {
        projectRepository.deleteProject(project)
    }
}