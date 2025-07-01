package com.mohammed.planity.presentation.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.presentation.main.home.components.EmptyStateContent
import com.mohammed.planity.presentation.main.home.components.HomeHeader
import com.mohammed.planity.presentation.main.home.components.SearchAndFilterSection
import com.mohammed.planity.presentation.main.home.model.HomeState
import com.mohammed.planity.ui.presentation.home.components.TaskListContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = koinViewModel(),
    onSettingsClick: () -> Unit,
    onAddCategoryClick: () -> Unit, // <-- ADD THIS
    onNavigateToTaskInfo: (String) -> Unit // Add this
) {
    val state by viewModel.state.collectAsState()


    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onSettingsClick = onSettingsClick,
        onAddCategoryClick = onAddCategoryClick ,
     onTaskClick = { task -> onNavigateToTaskInfo(task.id) }
    )
}
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onSettingsClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HomeHeader(userName = "Planity", onSettingsClick = onSettingsClick)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchAndFilterSection(
                searchQuery = state.searchQuery,
                availableCategories = state.availableCategories,
                selectedFilter = state.selectedFilter,
                onSearchQueryChanged = { query -> onEvent(HomeEvent.OnSearchQueryChanged(query)) },
                onFilterChanged = { filter -> onEvent(HomeEvent.OnFilterChanged(filter)) },
                onAddCategoryClick = onAddCategoryClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                // --- THIS IS THE NEW, CORRECTED FILTERING LOGIC ---

                // Determine which tasks to display based on the selected filter chip
                val tasksToDisplay = when (state.selectedFilter) {
                    "All Task" -> {
                        // **FIX 1:** Show tasks that are NOT in "Completed" AND NOT in "Trash"
                        state.tasks.filter { it.category != "Completed" && it.category != "Trash" }
                    }
                    "Completed" -> {
                        state.tasks.filter { it.category == "Completed" }
                    }
                    "Trash" -> {
                        state.tasks.filter { it.category == "Trash" }
                    }
                    else -> {
                        // For specific categories like "Work", show only tasks in that category
                        state.tasks.filter { it.category.equals(state.selectedFilter, ignoreCase = true) }
                    }
                }

                // Apply the search query on top of the already filtered list
                val finalFilteredTasks = tasksToDisplay.filter {
                    it.title.contains(state.searchQuery, ignoreCase = true)
                }

                // --- END OF CORRECTION ---

                if (finalFilteredTasks.isEmpty()) {
                    // Show the empty state if the final list is empty
                    EmptyStateContent()
                } else {
                    TaskListContent(
                        tasks = finalFilteredTasks,
                        onTaskClick = onTaskClick,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}