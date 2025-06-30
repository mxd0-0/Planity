package com.mohammed.planity.domain.use_case


import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        if (taskId.isNotBlank()) {
            repository.deleteTask(taskId)
        }
    }
}
