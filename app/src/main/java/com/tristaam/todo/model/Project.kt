package com.tristaam.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_table")
class Project(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}