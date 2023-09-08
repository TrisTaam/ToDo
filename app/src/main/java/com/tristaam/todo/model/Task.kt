package com.tristaam.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(val name: String, val description: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}