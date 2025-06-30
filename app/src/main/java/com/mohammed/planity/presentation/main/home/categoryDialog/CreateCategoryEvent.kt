package com.mohammed.planity.presentation.main.home.categoryDialog

sealed class CreateCategoryEvent {
    data class OnNameChanged(val name: String) : CreateCategoryEvent()
    object OnSaveClicked : CreateCategoryEvent()
}