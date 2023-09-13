package com.tristaam.todo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristaam.todo.model.Category
import com.tristaam.todo.room.category.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(context: Context) : ViewModel() {
    private val categoryRepository: CategoryRepository

    init {
        categoryRepository = CategoryRepository(context)
    }

    fun insertCategory(category: Category) = viewModelScope.launch {
        categoryRepository.insertCategory(category)
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        categoryRepository.updateCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        categoryRepository.deleteCategory(category)
    }

    fun getAllCategories() = categoryRepository.getAllCategories()
}