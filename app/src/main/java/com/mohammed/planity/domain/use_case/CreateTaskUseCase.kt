package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * This use case can contain validation logic before creating a task.
     * For example, checking if the title is not empty.
     */
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            // In a real app, you would throw a custom exception here
            // that the ViewModel can catch and show an error message.
            println("Task title cannot be empty.")
            return
        }
        repository.createTask(task)
    }
}