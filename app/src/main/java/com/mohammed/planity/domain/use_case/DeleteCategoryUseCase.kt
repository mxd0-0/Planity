package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(categoryId: String) {
        repository.deleteCategory(categoryId)
    }
}