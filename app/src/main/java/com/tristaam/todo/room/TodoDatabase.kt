package com.tristaam.todo.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.tristaam.todo.model.Category
import com.tristaam.todo.model.Task
import com.tristaam.todo.room.category.CategoryDao
import com.tristaam.todo.room.task.TaskDao

@Database(entities = [Task::class, Category::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

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