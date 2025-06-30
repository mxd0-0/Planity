package com.mohammed.planity.presentation.main.home.categoryDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.use_case.CreateCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateCategoryViewModel (
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateCategoryState())
    val state = _state.asStateFlow()

    fun onEvent(event: CreateCategoryEvent) {
        when (event) {
            is CreateCategoryEvent.OnNameChanged -> {
                _state.update { it.copy(categoryName = event.name) }
            }
            CreateCategoryEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(isSaving = true) }
                    createCategoryUseCase(state.value.categoryName)
                    _state.update { it.copy(isSaving = false, isCategorySaved = true) }
                }
            }
        }
    }
}