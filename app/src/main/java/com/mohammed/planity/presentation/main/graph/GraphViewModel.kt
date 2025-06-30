package com.mohammed.planity.presentation.main.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.planity.domain.use_case.GetWeeklyTaskStatsUseCase
import com.mohammed.planity.ui.presentation.graph.GraphState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GraphViewModel (
    private val getWeeklyTaskStatsUseCase: GetWeeklyTaskStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GraphState())
    val state = _state.asStateFlow()

    init {
        fetchStats()
    }

    private fun fetchStats() {
        getWeeklyTaskStatsUseCase().onEach { stats ->
            _state.update { it.copy(stats = stats, isLoading = false) }
        }.launchIn(viewModelScope)
    }
}