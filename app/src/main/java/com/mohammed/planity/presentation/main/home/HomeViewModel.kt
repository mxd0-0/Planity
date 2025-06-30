package com.mohammed.planity.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.use_case.CreateTaskUseCase
import com.mohammed.planity.domain.use_case.GetCategoriesUseCase
import com.mohammed.planity.domain.use_case.GetTasksUseCase
import com.mohammed.planity.presentation.main.home.model.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.map // <-- IMPORTANT IMPORT

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.mohammed.planity.domain.use_case.MoveTaskToCategoryUseCase // <-- And this
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel  (
    private val getTasksUseCase: GetTasksUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val moveTaskToCategoryUseCase: MoveTaskToCategoryUseCase,
    private val createTaskUseCase: CreateTaskUseCase // We are re-adding this
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        fetchTasks()
        fetchCategories()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
            }
            is HomeEvent.OnFilterChanged -> {
                _state.update { it.copy(selectedFilter = event.filter) }
            }

            // This event is now mapped to our "move to category" logic
            is HomeEvent.OnTaskCheckedChanged -> {
                viewModelScope.launch {
                    if (event.isChecked) {
                        // If checked, move to "Completed"
                        moveTaskToCategoryUseCase(taskId = event.task.id, newCategory = "Completed")
                    } else {
                        // This logic is for un-checking a task. For now, we'll default it back
                        // to the "Work" category as a placeholder. A real implementation
                        // would need to store the task's original category.
                        moveTaskToCategoryUseCase(taskId = event.task.id, newCategory = "Work")
                    }
                }
            }

            // The `OnTaskDoneClicked` event can have the same logic as checking a box.
            is HomeEvent.OnTaskDoneClicked -> {
                viewModelScope.launch {
                    moveTaskToCategoryUseCase(taskId = event.task.id, newCategory = "Completed")
                }
            }

            // This placeholder logic for creating a task is now re-enabled.
            HomeEvent.OnCreateTaskClicked -> {
                viewModelScope.launch {
                    // Create a simple date string for the new task
                    val sdf = SimpleDateFormat("d,MMMM,yyyy", Locale.getDefault())
                    val currentDate = sdf.format(Date())

                    val newTask = Task(
                        id = "", // Firestore generates this
                        title = "A New Task from the App",
                        date = currentDate,
                        category = "Work",
                        isHighPriority = false
                    )
                    createTaskUseCase(newTask)
                }
            }
        }
    }

    private fun fetchTasks() {
        _state.update { it.copy(isLoading = true) }
        getTasksUseCase().onEach { tasks ->
            _state.update {
                it.copy(isLoading = false, tasks = tasks)
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchCategories() {
        getCategoriesUseCase()
            .map { categoryObjects -> categoryObjects.map { it.name } }
            .onEach { categoryNamesFromDb ->
                // --- THIS IS THE UPDATED LOGIC ---
                // 1. Start with our static, predefined filters.
                val staticFilters = listOf("All Task", "Completed", "Trash")

                // 2. Combine them with the dynamic categories from Firestore.
                val fullFilterList = staticFilters + categoryNamesFromDb

                _state.update {
                    // 3. Update the state with the complete, distinct list.
                    it.copy(availableCategories = fullFilterList.distinct())
                }
            }
            .launchIn(viewModelScope)
    }
}