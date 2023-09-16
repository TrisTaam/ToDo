package com.tristaam.todo.ui.dialog

import com.tristaam.todo.model.Project

interface ICreateProjectListener {
    fun onCreateProject(project: Project)
}