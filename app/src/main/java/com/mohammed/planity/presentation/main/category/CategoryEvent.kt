package com.mohammed.planity.ui.presentation.category

sealed class CategoryEvent {
    data class OnDeleteCategory(val categoryId: String) : CategoryEvent()
    data class OnMoveCategory(val fromIndex: Int, val toIndex: Int) : CategoryEvent()
    object OnCreateCategoryClicked : CategoryEvent()
}