package com.mohammed.planity.presentation.main.home.createtask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.ui.theme.PlanityTheme
import com.mohammed.planity.R
import com.mohammed.planity.ui.presentation.createtask.CreateTaskViewModel
import com.mohammed.planity.ui.theme.ChipYellow
import com.mohammed.planity.ui.theme.errorContainer
import org.koin.androidx.compose.koinViewModel


@Composable
fun CreateTaskScreenRoute(
    viewModel: CreateTaskViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isTaskSaved) {
        if (state.isTaskSaved) {
            onDismiss()
        }
    }
    CreateTaskScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    state: CreateTaskState,
    onEvent: (CreateTaskEvent) -> Unit,
    onDismiss: () -> Unit
) {
    // A Box to create the dimming effect (scrim)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss, enabled = !state.isSaving)
            .imePadding()                 // Pushes dialog up when keyboard appears
            .navigationBarsPadding()     // Handles gesture nav bar
            .verticalScroll(rememberScrollState())// This is crucial for pushing content above the keyboard
                 ,
        contentAlignment = Alignment.Center
    ) {
        // This Box contains the dialog and the close button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Prevent clicks on this Box from passing through to the scrim
                .clickable(enabled = false, onClick = {})
        ) {
            // Main dialog card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp), // Make space for the close button
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val focusRequester = remember { FocusRequester() }

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    TextField(
                        value = state.title,
                        onValueChange = { onEvent(CreateTaskEvent.OnTitleChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .focusRequester(focusRequester),
                        placeholder = { Text("Input new task here...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionRow(state = state, onEvent = onEvent)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onEvent(CreateTaskEvent.OnSaveTaskClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = !state.isSaving && state.title.isNotBlank()
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Add New task", fontSize = 18.sp)
                        }
                    }
                }
            }

            // Close button, placed above the main card
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.TopCenter),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface
            ) {
                IconButton(onClick = onDismiss, enabled = !state.isSaving) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        }
    }
}


@Composable
fun ActionRow(state: CreateTaskState, onEvent: (CreateTaskEvent) -> Unit) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Dropdown
        Box {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { categoryMenuExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(state.selectedCategory, color = MaterialTheme.colorScheme.onSurface)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select category")
            }
            DropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = { categoryMenuExpanded = false }
            ) {
                state.availableCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            onEvent(CreateTaskEvent.OnCategoryChanged(category))
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Priority and Date buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val priorityColor = if (state.isHighPriority) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            IconButton(
                onClick = { onEvent(CreateTaskEvent.OnPriorityToggle) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(priorityColor)
            ) {
                Icon(painter = painterResource(R.drawable.flag), contentDescription = "Set priority", tint = if (state.isHighPriority) errorContainer else ChipYellow)
            }
            IconButton(
                onClick = { /* TODO: Show Date Picker */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Set date")
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0x80000000)
@Composable
fun CreateTaskScreenPreview() {
    PlanityTheme {
        CreateTaskScreen(
            state = CreateTaskState(),
            onEvent = {},
            onDismiss = {}
        )
    }
}