package com.tristaam.todo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "task_table",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    var title: String,
    var description: String,
    var projectId: Int,
    var dueDate: Date,
    var status: Boolean,
    var priority: Priority
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}