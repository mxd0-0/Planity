package com.mohammed.planity.domain.repository


import com.mohammed.planity.domain.model.Category
import com.mohammed.planity.domain.model.Task
import kotlinx.coroutines.flow.Flow


interface TaskRepository {

    suspend fun deleteTask(taskId: String)
    fun getTasks(): Flow<List<Task>>
    suspend fun createTask(task: Task)
    fun getTaskById(taskId: String): Flow<Task?> // Nullable in case the task is not found
   // suspend fun deleteTask(taskId: String)
    suspend fun updateTask(task: Task)
    //category
    fun getCategories(): Flow<List<Category>>
    suspend fun createCategory(categoryName: String)
    suspend fun deleteCategory(categoryId: String)
    suspend fun updateCategoryOrder(categories: List<Category>)
    suspend fun moveTaskToCategory(taskId: String, newCategory: String)
}