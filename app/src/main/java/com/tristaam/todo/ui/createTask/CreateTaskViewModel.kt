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
import com.tristaam.todo.model.Priority
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class CreateTaskViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    private val subtaskRepository: SubtaskRepository
    var projectId: Int = 0
    private var _subtasks = MutableLiveData<List<Subtask>>()
    val subtasks get() = _subtasks.value?.toMutableList() ?: mutableListOf()
    private var _priority = MutableLiveData<Priority>()
    val priority get() = _priority
    lateinit var remindBefore: Date

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
        subtaskRepository = SubtaskRepository(context)
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val taskId = taskRepository.insertTask(task)
        subtasks.forEach {
            it.taskId = taskId
            subtaskRepository.insertSubtask(it)
            Log.d("CreateTaskViewModel", "insertTask: $it")
        }
    }

    fun getProject(id: Int): LiveData<Project> = projectRepository.getProject(id)

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