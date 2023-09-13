package com.tristaam.todo.room.category

import android.content.Context
import com.tristaam.todo.model.Category
import com.tristaam.todo.room.TodoDatabase

class CategoryRepository(context: Context) {
    private val categoryDao: CategoryDao

    init {
        categoryDao = TodoDatabase.getInstance(context).categoryDao()
    }

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    fun getAllCategories() = categoryDao.getAllCategories()
}