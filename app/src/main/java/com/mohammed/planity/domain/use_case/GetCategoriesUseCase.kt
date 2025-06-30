package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.model.Category
import com.mohammed.planity.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories().combine(repository.getTasks()) { categories, tasks ->
            categories.map { category ->
                val count = tasks.count { it.category == category.name }
                category.copy(taskCount = count)
            }
        }
    }
}