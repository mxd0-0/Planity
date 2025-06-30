package com.mohammed.planity.ui.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.use_case.DeleteCategoryUseCase
import com.mohammed.planity.domain.use_case.GetCategoriesUseCase
import com.mohammed.planity.domain.use_case.UpdateCategoryOrderUseCase
import com.mohammed.planity.presentation.main.category.CategoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class CategoryViewModel (
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryOrderUseCase: UpdateCategoryOrderUseCase
) : ViewModel()
{

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    init {
        fetchCategories()
    }

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnMoveCategory -> {
                // --- THIS IS THE CORRECTED, ROBUST LOGIC ---
                _state.update { currentState ->
                    val initialList = currentState.categories
                    val fromIndex = event.fromIndex
                    var toIndex = event.toIndex

                    // Create a mutable copy to work with
                    val mutableList = initialList.toMutableList()

                    // The item being moved
                    val movedItem = mutableList.removeAt(fromIndex)

                    // Adjust the 'toIndex' if we are moving an item downwards.
                    // When an item is removed, the indices of all subsequent items shift up.
                    if (fromIndex < toIndex) {
                        toIndex--
                    }

                    // Add the item to its new position.
                    // This is now safe and will not go out of bounds.
                    mutableList.add(toIndex, movedItem)

                    // Launch a coroutine to save the new order to Firestore
                    viewModelScope.launch {
                        updateCategoryOrderUseCase(mutableList)
                    }

                    // Return the new state for optimistic UI update
                    currentState.copy(categories = mutableList)
                }
            }
            is CategoryEvent.OnDeleteCategory -> {
                viewModelScope.launch {
                    deleteCategoryUseCase(event.categoryId)
                }
            }
            CategoryEvent.OnCreateCategoryClicked -> {}
        }
    }

    private fun fetchCategories() {
        getCategoriesUseCase().onEach { categories ->
            _state.update { it.copy(categories = categories) }
        }.launchIn(viewModelScope)
    }
}