package com.tristaam.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_table")
class Project(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)