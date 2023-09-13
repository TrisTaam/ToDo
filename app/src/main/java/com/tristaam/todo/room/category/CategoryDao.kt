package com.tristaam.todo.room.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tristaam.todo.model.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): LiveData<List<Category>>
}