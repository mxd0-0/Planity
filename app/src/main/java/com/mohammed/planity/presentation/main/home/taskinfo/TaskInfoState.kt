package com.mohammed.planity.presentation.main.home.taskinfo

import com.mohammed.planity.domain.model.Task

data class TaskInfoState(
    // The original, unmodified task loaded from the database
    val initialTask: Task? = null,

    // Editable fields that the user can modify in the UI
    val editedTitle: String = "",
    val editedDescription: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    val editedCategory: String = "",
    val isHighPriority: Boolean = false,
    val isNotificationEnabled: Boolean = false,

    // Control flags for the UI
    val isLoading: Boolean = true,
    val isTaskDeleted: Boolean = false, // We'll keep this name, but it now means "moved to trash"
    val isTaskSaved: Boolean = false,
    val error: String? = null
)