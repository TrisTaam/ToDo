package com.tristaam.todo.database.subtask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tristaam.todo.model.Subtask

@Dao
interface SubtaskDao {
    @Insert
    suspend fun insertSubtask(subtask: Subtask)

    @Update
    suspend fun updateSubtask(subtask: Subtask)

    @Delete
    suspend fun deleteSubtask(subtask: Subtask)

    @Query("SELECT * FROM subtask_table")
    fun getAllSubtasks(): LiveData<List<Subtask>>
}