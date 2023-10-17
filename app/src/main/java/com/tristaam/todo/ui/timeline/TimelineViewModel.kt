package com.tristaam.todo.ui.timeline

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.database.task.TaskRepository
import com.tristaam.todo.model.Task
import com.tristaam.todo.utils.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TimelineViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    init {
        taskRepository = TaskRepository(context)
    }

    fun setTasksByDate(startDate: Date, endDate: Date, lifecycleOwner: LifecycleOwner) {
        taskRepository.getTasksByDate(startDate, endDate).observeOnce(lifecycleOwner) {
            _tasks.value = it
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

    class TimelineViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TimelineViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}