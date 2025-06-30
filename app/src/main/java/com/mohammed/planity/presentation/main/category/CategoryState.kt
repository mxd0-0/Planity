package com.mohammed.planity.presentation.main.category

import com.mohammed.planity.domain.model.Category

data class CategoryState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)