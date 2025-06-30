package com.mohammed.planity.ui.presentation.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohammed.planity.R
import com.mohammed.planity.domain.model.Category
import com.mohammed.planity.presentation.main.category.CategoryState
import com.mohammed.planity.ui.theme.PlanityTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryScreenRoute(
    viewModel: CategoryViewModel = koinViewModel(),
    onCreateCategory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    CategoryScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onCreateCategory = onCreateCategory,
        onNavigateToSettings = onNavigateToSettings
    )
}

@Composable
fun CategoryScreen(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    onCreateCategory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            // The indices from the library will now correctly match our data list
            onEvent(CategoryEvent.OnMoveCategory(from.index, to.index))
        }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CategoryTopBar(onSettingsClick = onNavigateToSettings)
        }
    ) { paddingValues ->
        // Use a parent Column to hold headers, the list, and the footer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // --- HEADER ITEMS (Non-reorderable) ---
            Spacer(modifier = Modifier.height(16.dp))
            Text("Categories display on homepage", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Manage Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- REORDERABLE LIST ---
            LazyColumn(
                state = reorderableState.listState,
                modifier = Modifier
                    .weight(1f) // Let the list fill the available space
                    .reorderable(reorderableState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.categories, key = { it.id }) { category ->
                    ReorderableItem(reorderableState, key = category.id) {
                        CategoryItem(
                            category = category,
                            modifier = Modifier.detectReorderAfterLongPress(reorderableState),
                            onDeleteClick = { onEvent(CategoryEvent.OnDeleteCategory(category.id)) }
                        )
                    }
                }
            }

            // --- FOOTER ITEM (Non-reorderable) ---
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onCreateCategory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create new category", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create new category", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CategoryTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Category", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(Icons.Outlined.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.drag),
                contentDescription = "Drag Handle",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Lock, contentDescription = category.name, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(category.name, fontWeight = FontWeight.SemiBold)
                // You might want to adjust the "7" or make it dynamic
                Text("${category.taskCount}/7 task", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = { /* TODO */ menuExpanded = false })
                    DropdownMenuItem(text = { Text("Delete", color = MaterialTheme.colorScheme.error) }, onClick = { onDeleteClick(); menuExpanded = false })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    PlanityTheme {
        val previewState = CategoryState(
            categories = listOf(
                Category("1", "Office Work", 1, 5),
                Category("2", "Personal Task", 2, 3),
                Category("3", "Wishlist", 3, 5)
            )
        )
        CategoryScreen(
            state = previewState,
            onEvent = {},
            onCreateCategory = {},
            onNavigateToSettings = {}
        )
    }
}