package com.mohammed.planity.presentation.main.home.createtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.use_case.CreateTaskUseCase
import com.mohammed.planity.domain.use_case.GetCategoriesUseCase // <-- IMPORT THIS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskViewModel (
    private val createTaskUseCase: CreateTaskUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase // <-- INJECT THIS
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTaskState())
    val state = _state.asStateFlow()

    init {
        // --- ADD THIS CALL ---
        fetchAvailableCategories()
    }

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.OnTitleChanged -> {
                _state.update { it.copy(title = event.title) }
            }
            is CreateTaskEvent.OnCategoryChanged -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }
            CreateTaskEvent.OnPriorityToggle -> {
                _state.update { it.copy(isHighPriority = !it.isHighPriority) }
            }
            CreateTaskEvent.OnSaveTaskClicked -> {
                saveTask()
            }
        }
    }

    private fun fetchAvailableCategories() {
        getCategoriesUseCase()
            // We only need the names for the dropdown
            .map { categoryObjects -> categoryObjects.map { it.name } }
            .onEach { categoryNames ->
                _state.update {
                    // Update the list and ensure the selectedCategory is valid
                    it.copy(
                        availableCategories = categoryNames,
                        // If the default "Work" doesn't exist, pick the first available one
                        selectedCategory = it.selectedCategory.takeIf { categoryNames.contains(it) } ?: categoryNames.firstOrNull() ?: "Work"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun saveTask() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val sdf = SimpleDateFormat("d,MMMM,yyyy", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val newTask = Task(
                id = "", // Firestore will generate this
                title = _state.value.title.trim(),
                date = currentDate,
                category = _state.value.selectedCategory,
                isHighPriority = _state.value.isHighPriority
            )

            if (newTask.title.isNotBlank()) {
                createTaskUseCase(newTask)
                _state.update { it.copy(isSaving = false, isTaskSaved = true) }
            } else {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }
}