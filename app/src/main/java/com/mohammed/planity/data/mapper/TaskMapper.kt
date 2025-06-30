package com.mohammed.planity.data.mapper


import com.mohammed.planity.data.model.TaskDto
import com.mohammed.planity.domain.model.Task

// Mappers prevent data source models (DTOs) from leaking into the domain layer.
fun TaskDto.toDomain(): Task {
    return Task(
        id = this.id ?: "",
        title = this.title ?: "Untitled",
        date = this.date ?: "",
        category = this.category ?: "Uncategorized",
        isHighPriority = this.isHighPriority ?: false,

        // --- ADD THIS MAPPING ---
        completedAt = this.completedAt // Can be null
    )
}

// From Domain Model -> Firestore DTO
fun Task.toDto(): TaskDto {
    return TaskDto(
        id = this.id,
        title = this.title,
        date = this.date,
        category = this.category,
        isHighPriority = this.isHighPriority,

        // --- ADD THIS MAPPING ---
        completedAt = this.completedAt
    )
}
