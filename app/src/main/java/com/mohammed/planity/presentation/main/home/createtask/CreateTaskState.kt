package com.mohammed.planity.presentation.main.home.createtask

// Represents the state of the CreateTask screen UI
data class CreateTaskState(
    val title: String = "",
    // Correct: Start with an empty list, which will be populated from Firestore.
    val availableCategories: List<String> = emptyList(),
    // Correct: The default selected category can be a sensible default or the first from the list.
    val selectedCategory: String = "Work",
    val isHighPriority: Boolean = false,
    val isSaving: Boolean = false,
    val isTaskSaved: Boolean = false
)