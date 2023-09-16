package com.tristaam.todo.database.project

import android.content.Context
import com.tristaam.todo.database.TodoDatabase
import com.tristaam.todo.model.Project

class ProjectRepository(context: Context) {
    private val projectDao: ProjectDao

    init {
        projectDao = TodoDatabase.getInstance(context).projectDao()
    }

    suspend fun insertProject(project: Project) = projectDao.insertProject(project)
    suspend fun updateProject(project: Project) = projectDao.updateProject(project)
    suspend fun deleteProject(project: Project) = projectDao.deleteProject(project)
    fun getAllProjects() = projectDao.getAllProjects()
}