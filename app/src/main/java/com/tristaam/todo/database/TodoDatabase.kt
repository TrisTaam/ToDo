package com.tristaam.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tristaam.todo.database.project.ProjectDao
import com.tristaam.todo.database.subtask.SubtaskDao
import com.tristaam.todo.database.task.TaskDao
import com.tristaam.todo.model.Project
import com.tristaam.todo.model.Subtask
import com.tristaam.todo.model.Tag
import com.tristaam.todo.model.Task

@Database(
    entities = [
        Task::class,
        Project::class,
        Subtask::class,
        Tag::class], version = 1
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
    abstract fun subtaskDao(): SubtaskDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}