package com.mohammed.planity.presentation.main.home.createtask

sealed class CreateTaskEvent {
    data class OnTitleChanged(val title: String) : CreateTaskEvent()
    data class OnCategoryChanged(val category: String) : CreateTaskEvent()
    object OnPriorityToggle : CreateTaskEvent()
    object OnSaveTaskClicked : CreateTaskEvent()
}