package com.mohammed.planity.presentation.main.home.model

import com.mohammed.planity.domain.model.Task

data class HomeState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "Vasudev Krishna", // Default name, can be loaded from user profile later
    val searchQuery: String = "",
    val error: String? = null,
    val selectedFilter: String = "All Task",
    val availableCategories: List<String> = listOf("All Task", "Work", "Personal"),
)