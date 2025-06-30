package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.model.Category
import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateCategoryOrderUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(categories: List<Category>) {
        repository.updateCategoryOrder(categories)
    }
}