package com.mohammed.planity.presentation.main.home.taskinfo

sealed class TaskInfoEvent {
    data class OnTitleChanged(val title: String) : TaskInfoEvent()
    data class OnDescriptionChanged(val description: String) : TaskInfoEvent()
    data class OnCategoryChanged(val category: String) : TaskInfoEvent()
    object OnPriorityToggle : TaskInfoEvent()
    data class OnNotificationToggled(val isEnabled: Boolean) : TaskInfoEvent()
    object OnSaveChangesClicked : TaskInfoEvent()
    object OnDeleteClicked : TaskInfoEvent()
    object OnToggleCompletion : TaskInfoEvent()
}

