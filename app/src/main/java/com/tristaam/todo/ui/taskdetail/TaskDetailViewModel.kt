package com.tristaam.todo.ui.taskdetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.database.subtask.SubtaskRepository
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDetailViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    private val subtaskRepository: SubtaskRepository
    var projectId: Int = 0
    lateinit var task: Task
    private var _subtasks = MutableLiveData<List<Subtask>>()
    val subtasks get() = _subtasks.value?.toMutableList() ?: mutableListOf()
    private var _priority = MutableLiveData<Priority>()
    val priority get() = _priority

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
        subtaskRepository = SubtaskRepository(context)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
        subtaskRepository.deleteSubtasksByTaskId(task.id)
        subtasks.forEach {
            subtaskRepository.insertSubtask(it)
        }
    }

    fun getTaskById(taskId: Int): LiveData<Task> = taskRepository.getTaskById(taskId)

    fun getProject(id: Int): LiveData<Project> = projectRepository.getProject(id)

    fun getSubtasksByTaskId(taskId: Int): LiveData<List<Subtask>> =
        subtaskRepository.getSubtasksByTaskId(taskId)

    fun addSubtask(subtask: Subtask) {
        val list = subtasks
        list.add(subtask)
        _subtasks.value = list
    }

    fun deleteSubtask(position: Int) {
        val list = subtasks
        list.removeAt(position)
        _subtasks.value = list
    }

    fun updateSubtaskStatus(position: Int) {
        val list = this.subtasks
        list[position].status = !list[position].status
        _subtasks.value = list
    }

    fun updateSubtaskName(position: Int, newName: String) {
        val list = this.subtasks
        list[position].name = newName
        _subtasks.value = list
    }

    fun setPriority(priority: Priority) {
        _priority.value = priority
    }

    fun setSubtasks(subtasks: List<Subtask>) {
        _subtasks.value = subtasks
    }

    class TaskDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskDetailViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}