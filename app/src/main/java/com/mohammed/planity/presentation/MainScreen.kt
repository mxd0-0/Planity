package com.mohammed.planity.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mohammed.planity.presentation.navigation.AppBottomNavigationBar
import com.mohammed.planity.presentation.navigation.AppNavHost
import com.mohammed.planity.presentation.navigation.Destinations
import com.mohammed.planity.presentation.navigation.bottomNavItems
import com.mohammed.planity.ui.theme.PlanityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // --- START OF CHANGES ---

    // 1. Get the current back stack entry to know the current route.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define which routes should show the bottom bar.
    // We get the routes from the `bottomNavItems` list we already have.
    val routesWithBottomBar = bottomNavItems.map { it.route  }

    // 3. A boolean to easily check if the bar should be shown.
    val shouldShowBottomBar = currentRoute in routesWithBottomBar

    // --- END OF CHANGES ---

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AppBottomNavigationBar(navController = navController) { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
        floatingActionButton = {
            // --- THIS IS THE CORRECTED LOGIC ---
            // Show the FAB only if we are on the HomeScreen.
            AnimatedVisibility(
                visible = currentRoute == Destinations.Home.route, // More specific condition
                enter = slideInVertically(initialOffsetY = { it * 3 }),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Destinations.CreateTask.route) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            AppNavHost(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PlanityTheme {
        MainScreen()
    }
}