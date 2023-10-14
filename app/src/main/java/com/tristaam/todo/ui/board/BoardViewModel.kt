package com.tristaam.todo.ui.board

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.project.ProjectRepository
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Task
import com.tristaam.todo.utils.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardViewModel(context: Context ) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    private var _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> get() = _tasks
    private var _completedTasksCount: MutableLiveData<Int> = MutableLiveData(0)
    val completedTasksCount: LiveData<Int> get() = _completedTasksCount
    var tasksCount: Int = 0

    init {
        taskRepository = TaskRepository(context)
        projectRepository = ProjectRepository(context)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

    fun getAllTasks(): LiveData<List<Task>> = taskRepository.getAllTasks()

    private fun getAllTasksCount(): LiveData<Int> = taskRepository.getAllTasksCount()

    fun getAllProjects(): LiveData<List<Project>> = projectRepository.getAllProjects()

    fun getUncompletedTasksByProjectId(projectId: Int): LiveData<List<Task>> =
        taskRepository.getUncompletedTasksByProjectId(projectId)

    private fun getTasksCountByProjectId(projectId: Int): LiveData<Int> =
        taskRepository.getTasksCountByProjectId(projectId)

    private fun getCompletedTasksCountByProjectId(projectId: Int): LiveData<Int> =
        taskRepository.getCompletedTasksCountByProjectId(projectId)

    private fun getAllCompletedTasksCount(): LiveData<Int> =
        taskRepository.getAllCompletedTasksCount()

    fun setTasks(tasks: List<Task>, projectId: Int, lifecycleOwner: LifecycleOwner) {
        _tasks.value = tasks
        setCounter(projectId, lifecycleOwner)
    }

    private fun setCounter(projectId: Int, lifecycleOwner: LifecycleOwner) {
        if (projectId > 0) {
            getTasksCountByProjectId(projectId).observeOnce(lifecycleOwner) { it ->
                tasksCount = it
            }
            getCompletedTasksCountByProjectId(projectId).observeOnce(lifecycleOwner) { it ->
                _completedTasksCount.value = it
            }
        } else {
            getAllTasksCount().observeOnce(lifecycleOwner) { it ->
                tasksCount = it
            }
            getAllCompletedTasksCount().observeOnce(lifecycleOwner) { it ->
                _completedTasksCount.value = it
            }
        }
    }

    fun incCompletedTasksCount() {
        _completedTasksCount.value = _completedTasksCount.value?.plus(1)
    }

    fun decCompletedTasksCount() {
        _completedTasksCount.value = _completedTasksCount.value?.minus(1)
    }

    class BoardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BoardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BoardViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}