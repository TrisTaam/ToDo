package com.tristaam.todo.database.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tristaam.todo.model.Task
import java.util.Date

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE projectId = :projectId")
    fun getTasksByProjectId(projectId: Int): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM task_table WHERE status = 1")
    fun getAllCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE status = 0")
    fun getAllUncompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE status = 1 AND projectId = :projectId")
    fun getCompletedTasksByProjectId(projectId: Int): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE status = 0 AND projectId = :projectId")
    fun getUncompletedTasksByProjectId(projectId: Int): LiveData<List<Task>>

    @Query("SELECT COUNT(*) FROM task_table WHERE projectId = :projectId")
    fun getTasksCountByProjectId(projectId: Int): LiveData<Int>

    @Query("SELECT COUNT(*) FROM task_table")
    fun getAllTasksCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM task_table WHERE status = 1 AND projectId = :projectId")
    fun getCompletedTasksCountByProjectId(projectId: Int): LiveData<Int>

    @Query("SELECT COUNT(*) FROM task_table WHERE status = 1")
    fun getAllCompletedTasksCount(): LiveData<Int>

    @Query("SELECT * FROM task_table WHERE dueDate BETWEEN :startDate AND :endDate")
    fun getTasksByDate(startDate: Date, endDate: Date): LiveData<List<Task>>
}