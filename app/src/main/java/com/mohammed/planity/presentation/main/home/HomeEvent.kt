package com.mohammed.planity.presentation.main.home

import com.mohammed.planity.domain.model.Task



sealed class HomeEvent {
    data class OnSearchQueryChanged(val query: String) : HomeEvent()
    data class OnFilterChanged(val filter: String) : HomeEvent()
    object OnCreateTaskClicked : HomeEvent() // Example event
    data class OnTaskCheckedChanged(val task: Task, val isChecked: Boolean) : HomeEvent()
    data class OnTaskDoneClicked(val task: Task) : HomeEvent()

}