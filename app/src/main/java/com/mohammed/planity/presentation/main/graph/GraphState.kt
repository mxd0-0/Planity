package com.mohammed.planity.ui.presentation.graph

import com.mohammed.planity.domain.use_case.TaskStats

data class GraphState(
    val stats: TaskStats? = null,
    val isLoading: Boolean = true
)