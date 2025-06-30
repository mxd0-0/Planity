package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        // Add validation logic here if needed (e.g., title not blank)
        repository.updateTask(task)
    }
}