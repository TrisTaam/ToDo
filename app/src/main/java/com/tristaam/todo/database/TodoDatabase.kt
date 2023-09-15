package com.tristaam.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.tristaam.todo.database.task.TaskDao
import com.tristaam.todo.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

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