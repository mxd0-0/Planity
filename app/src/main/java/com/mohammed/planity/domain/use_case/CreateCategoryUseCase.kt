package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(categoryName: String) {
        if (categoryName.isNotBlank()) {
            repository.createCategory(categoryName.trim())
        }
    }
}