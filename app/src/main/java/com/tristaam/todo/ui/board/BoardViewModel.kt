package com.tristaam.todo.ui.board

import android.content.Context
import android.util.Log
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

class BoardViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val projectRepository: ProjectRepository
    private var _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> get() = _tasks
    private var _completedTasksCount: MutableLiveData<Int> = MutableLiveData(0)
    val completedTasksCount: LiveData<Int> get() = _completedTasksCount
    var tasksCount: Int = 0

    enum class TaskSortBy {
        DUE_DATE_OLD_NEW,
        DUE_DATE_NEW_OLD,
        CREATED_OLD_NEW,
        CREATED_NEW_OLD,
        PRIORITY_HIGH_LOW,
        ALPHABETICAL_A_Z
    }

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
            getTasksCountByProjectId(projectId).observeOnce(lifecycleOwner) {
                tasksCount = it
            }
            getCompletedTasksCountByProjectId(projectId).observeOnce(lifecycleOwner) {
                _completedTasksCount.value = it
            }
        } else {
            getAllTasksCount().observeOnce(lifecycleOwner) {
                tasksCount = it
            }
            getAllCompletedTasksCount().observeOnce(lifecycleOwner) {
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

    fun sortTaskBy(sortBy: TaskSortBy) {
        when (sortBy) {
            TaskSortBy.DUE_DATE_OLD_NEW -> {
                _tasks.value = _tasks.value?.sortedBy { it.dueDate }
            }

            TaskSortBy.DUE_DATE_NEW_OLD -> {
                _tasks.value = _tasks.value?.sortedByDescending { it.dueDate }
                Log.d("BoardViewModel", _tasks.value.toString())
            }

            TaskSortBy.CREATED_OLD_NEW -> {
                _tasks.value = _tasks.value?.sortedBy { it.id }
            }

            TaskSortBy.CREATED_NEW_OLD -> {
                _tasks.value = _tasks.value?.sortedByDescending { it.id }
            }

            TaskSortBy.PRIORITY_HIGH_LOW -> {
                _tasks.value = _tasks.value?.sortedByDescending { it.priority.weight }
            }

            TaskSortBy.ALPHABETICAL_A_Z -> {
                _tasks.value = _tasks.value?.sortedBy { it.title }
            }
        }
    }

    fun setTaskFilter(keyword: String, lifecycleOwner: LifecycleOwner) {
        getAllTasks().observeOnce(lifecycleOwner) {
            val key: String = keyword.trim()
            _tasks.value =
                if (key == "") {
                    it
                } else {
                    it.filter { task ->
                        task.title.contains(key, true)
                    }
                }
        }
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