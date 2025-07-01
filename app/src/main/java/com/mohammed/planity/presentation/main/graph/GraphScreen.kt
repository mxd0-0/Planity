package com.mohammed.planity.presentation.main.graph

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.ui.presentation.graph.GraphState
import org.koin.androidx.compose.koinViewModel

@Composable
fun GraphScreenRoute(
    viewModel: GraphViewModel = koinViewModel(),
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    GraphScreen(state = state, onNavigateToSettings = onNavigateToSettings)
}

@Composable
fun GraphScreen(
    state: GraphState,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            GraphTopBar(onSettingsClick = onNavigateToSettings)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.stats != null) {
                // Call our new ManualBarChart composable
                ManualBarChart(state.stats.weeklyCompletion)
                StatCards(
                    completed = state.stats.completedTasks,
                    pending = state.stats.pendingTasks
                )
            }
        }
    }
}

@Composable
fun GraphTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Graph", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
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
fun ManualBarChart(weeklyData: Map<String, Int>) {
    var selectedDay by remember { mutableStateOf("Thu") } // Pre-select Thursday to match the design
    val maxValue = weeklyData.values.maxOrNull() ?: 1 // Avoid division by zero

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Completed task", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Use BoxWithConstraints to get the available height for the chart
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val chartHeight = this.maxHeight

                // Main row for the chart content (Y-Axis + Bars)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Y-Axis Labels
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("10", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("08", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("06", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("04", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("02", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("00", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Divider Line
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))

                    // Bars Area
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom // Bars grow from the bottom
                    ) {
                        weeklyData.forEach { (day, value) ->
                            val isSelected = day == selectedDay

                            // Calculate height fraction based on the max value
                            val heightFraction = animateFloatAsState(
                                targetValue = value.toFloat() / maxValue,
                                animationSpec = tween(durationMillis = 1000)
                            ).value

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable { selectedDay = day },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (isSelected) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White
                                    ) {
                                        Text(
                                            text = "%2d".format(value),
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                // The Bar itself
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                ) {
                                    // The small circle indicator
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.5f))
                                                .align(Alignment.TopCenter)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                // Day label (e.g., "Sun")
                                Text(text = day, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCards(completed: Int, pending: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), label = "Completed Takes", count = completed)
        StatCard(modifier = Modifier.weight(1f), label = "Pending Task", count = pending)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, label: String, count: Int) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

