package com.tristaam.todo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tag_table",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Tag(var name: String, var taskId: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}