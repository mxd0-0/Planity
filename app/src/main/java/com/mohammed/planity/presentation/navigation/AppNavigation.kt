package com.mohammed.planity.presentation.navigation


import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.mohammed.planity.presentation.main.graph.GraphScreenRoute
import com.mohammed.planity.presentation.main.home.HomeScreenRoute
import com.mohammed.planity.presentation.main.home.createtask.CreateTaskScreenRoute
import com.mohammed.planity.ui.presentation.category.CategoryScreenRoute
import com.mohammed.planity.ui.presentation.createcategory.CreateCategoryRoute
import com.mohammed.planity.ui.presentation.settings.SettingsScreen
import com.mohammed.planity.ui.presentation.taskinfo.TaskInfoRoute
import com.mohammed.planity.ui.theme.gray400

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Home.route
    ) {
        composable(Destinations.Home.route) {
            HomeScreenRoute(
                onSettingsClick = { /* ... */ },
                onAddCategoryClick = { navController.navigate(Destinations.CreateCategory.route) } // <-- ADD THIS
                , onNavigateToTaskInfo = { taskId ->
                    navController.navigate(Destinations.TaskInfo.withArgs(taskId))
                }
            )
        }
        composable(
            route = Destinations.TaskInfo.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        )
        {
            TaskInfoRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        dialog(Destinations.CreateCategory.route) {
            CreateCategoryRoute(onDismiss = { navController.popBackStack() })
        }
        composable(Destinations.Category.route ) {
            CategoryScreenRoute(
                onCreateCategory = { navController.navigate(Destinations.CreateCategory.route) },
                onNavigateToSettings = { navController.navigate(Destinations.Settings.route) }
            )
        }
        composable(Destinations.Graph.route) {
            GraphScreenRoute(
                onNavigateToSettings = { navController.navigate(Destinations.Settings.route) }
            )
        }

        // Other screens
        composable(Destinations.Settings.route) { SettingsScreen() }
        dialog(Destinations.CreateTask.route) {
            CreateTaskScreenRoute(
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}

// This BottomNavigationBar now iterates over our `bottomNavItems` list
@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                label = { Text(text = item.title, fontWeight = FontWeight.SemiBold) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (isSelected) item.selectedIcon else item.unSelectedIcon
                        ),
                        contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = gray400,
                    unselectedTextColor = gray400,
                    indicatorColor = Color.Transparent,

                    )
            )
        }
    }
}