package com.mohammed.planity.presentation.main.home.taskinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.use_case.DeleteTaskUseCase
import com.mohammed.planity.domain.use_case.GetTaskByIdUseCase
import com.mohammed.planity.domain.use_case.MoveTaskToCategoryUseCase
import com.mohammed.planity.domain.use_case.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskInfoViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val moveTaskToCategoryUseCase: MoveTaskToCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskInfoState())
    val state = _state.asStateFlow()

    private val taskId: String = checkNotNull(savedStateHandle.get("taskId"))

    init {
        fetchTaskDetails()
    }

    fun onEvent(event: TaskInfoEvent) {
        when (event) {
            is TaskInfoEvent.OnTitleChanged -> {
                _state.update { it.copy(editedTitle = event.title) }
            }
            is TaskInfoEvent.OnDescriptionChanged -> {
                _state.update { it.copy(editedDescription = event.description) }
            }
            is TaskInfoEvent.OnCategoryChanged -> {
                _state.update { it.copy(editedCategory = event.category) }
            }
            TaskInfoEvent.OnPriorityToggle -> {
                _state.update { it.copy(isHighPriority = !it.isHighPriority) }
            }
            is TaskInfoEvent.OnNotificationToggled -> {
                _state.update { it.copy(isNotificationEnabled = event.isEnabled) }
            }
            TaskInfoEvent.OnSaveChangesClicked -> {
                saveChanges()
            }

            // --- THIS IS THE SINGLE, CORRECTED DELETE LOGIC ---
            TaskInfoEvent.OnDeleteClicked -> {
                // --- THIS IS THE CORRECT LOGIC ---
                viewModelScope.launch {
                    // We move the task to the "Trash" category, NOT delete it from the database
                    moveTaskToCategoryUseCase(taskId, "Trash")

                    // This flag is still useful to tell the UI to navigate back
                    _state.update { it.copy(isTaskDeleted = true) }
                }
            }

            TaskInfoEvent.OnToggleCompletion -> {
                _state.value.initialTask?.let { task ->
                    viewModelScope.launch {
                        val newCategory = if (task.category == "Completed") "Work" else "Completed"
                        moveTaskToCategoryUseCase(task.id, newCategory)
                    }
                }
            }
        }
    }

    private fun fetchTaskDetails() {
        getTaskByIdUseCase(taskId).onEach { taskFromDb ->
            taskFromDb?.let {
                _state.update { currentState ->
                    currentState.copy(
                        initialTask = it,
                        editedTitle = if (currentState.initialTask == null) it.title else currentState.editedTitle,
                        editedCategory = if (currentState.initialTask == null) it.category else currentState.editedCategory,
                        isHighPriority = if (currentState.initialTask == null) it.isHighPriority else currentState.isHighPriority,
                        isLoading = false
                    )
                }
            } ?: run {
                _state.update { it.copy(isLoading = false, error = "Task not found.") }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveChanges() {
        viewModelScope.launch {
            val currentState = _state.value
            currentState.initialTask?.let { originalTask ->
                // The isCompleted field is gone from the model, so we remove it here.
                val updatedTask = originalTask.copy(
                    title = currentState.editedTitle.trim(),
                    category = currentState.editedCategory,
                    isHighPriority = currentState.isHighPriority
                )
                if (updatedTask.title.isNotBlank()) {
                    updateTaskUseCase(updatedTask)
                    _state.update { it.copy(isTaskSaved = true) }
                } else {
                    _state.update { it.copy(error = "Title cannot be empty.") }
                }
            }
        }
    }
}