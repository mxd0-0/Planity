
package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.repository.TaskRepository


import com.mohammed.planity.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject // Using Hilt/Dagger for DI


class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Fetches and applies business logic (e.g., sorting) to the tasks.
     * The ViewModel will call this to get data.
     */
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasks().map { tasks ->
            // Example of business logic: group by category, then sort by date
            tasks.sortedWith(compareBy({ it.category }, { it.date }))
        }
    }
}