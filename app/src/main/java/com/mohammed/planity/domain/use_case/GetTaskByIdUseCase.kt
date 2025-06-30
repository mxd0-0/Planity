package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(taskId: String) = repository.getTaskById(taskId)
}