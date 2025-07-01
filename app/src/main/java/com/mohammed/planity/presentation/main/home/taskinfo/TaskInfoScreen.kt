package com.mohammed.planity.presentation.main.home.taskinfo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohammed.planity.R
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful composable that connects to the ViewModel.
 */
@Composable
fun TaskInfoRoute(
    viewModel: TaskInfoViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isTaskDeleted, state.isTaskSaved) {
        if (state.isTaskDeleted || state.isTaskSaved) {
            onNavigateBack()
        }
    }

    TaskInfoScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


/**
 * The main stateless UI for the Task Info screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoScreen(
    state: TaskInfoState,
    onEvent: (TaskInfoEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                onBackClicked = onNavigateBack,
                onDeleteClicked = { onEvent(TaskInfoEvent.OnDeleteClicked) },
                onSaveClicked = { onEvent(TaskInfoEvent.OnSaveChangesClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.initialTask != null -> {
                    val task = state.initialTask
                    // A task is completed if its category is "Completed"
                    val isCompleted = task.category == "Completed"

                    val titleTextStyle = if (isCompleted) {
                        MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextField(
                        value = state.editedTitle,
                        onValueChange = { newTitle -> onEvent(TaskInfoEvent.OnTitleChanged(newTitle)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = titleTextStyle,
                        leadingIcon = {
                            Checkbox(
                                checked = isCompleted,
                                onCheckedChange = { onEvent(TaskInfoEvent.OnToggleCompletion) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Details Section
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoRow(icon = R.drawable.category_selected, label = "Category") {
                            Text(state.editedCategory)
                        }
                        InfoRow(icon = R.drawable.calendar, label = "Due Date") {
                            Text(task.date)
                        }
                        InfoRow(icon = R.drawable.time, label = "Time") {
                            Text("02:00 AM")
                        }
                        InfoRow(icon = R.drawable.flag, label = "Priority") {
                            Text(if (state.isHighPriority) "High" else "Low")
                        }
                        InfoRow(icon = R.drawable.outline_notifications_24, label = "Notify me") {
                            Switch(
                                checked = state.isNotificationEnabled,
                                onCheckedChange = { isEnabled -> onEvent(TaskInfoEvent.OnNotificationToggled(isEnabled)) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    TextField(
                        value = state.editedDescription,
                        onValueChange = { newDesc -> onEvent(TaskInfoEvent.OnDescriptionChanged(newDesc)) },
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text("Add a description...", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    )
                }
                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.error, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

/**
 * Top app bar with Back, Save, and Delete actions.
 */
@Composable
private fun TopBar(
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        // Action Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Save Button
            IconButton(
                onClick = onSaveClicked,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Changes", tint = MaterialTheme.colorScheme.onPrimary)
            }
            // Delete Button
            IconButton(
                onClick = onDeleteClicked,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x33E53935)) // Red with transparency
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = Color(0xFFE53935))
            }
        }
    }
}

/**
 * A reusable row for displaying task information.
 */
@Composable
private fun InfoRow(
    @DrawableRes icon: Int,
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            Box {
                content()
            }
        }
    }
}


