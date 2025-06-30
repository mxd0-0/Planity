package com.mohammed.planity.presentation.main.home.categoryDialog

data class CreateCategoryState(
    val categoryName: String = "",
    val isSaving: Boolean = false,
    val isCategorySaved: Boolean = false
)