package com.tristaam.todo.adapter.project

import com.tristaam.todo.model.Project

interface IProjectListener {
    fun onProjectClick(project: Project)
}