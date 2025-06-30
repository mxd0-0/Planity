package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class MoveTaskToCategoryUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, newCategory: String) {
        if (taskId.isNotBlank() && newCategory.isNotBlank()) {
            repository.moveTaskToCategory(taskId, newCategory)
        }
    }
}