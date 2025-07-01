package com.mohammed.planity.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.mohammed.planity.presentation.onBoarding.GetStartedScreen
import com.mohammed.planity.presentation.settings.SettingsScreenRoute
import com.mohammed.planity.ui.presentation.category.CategoryScreenRoute
import com.mohammed.planity.ui.presentation.createcategory.CreateCategoryRoute
import com.mohammed.planity.presentation.main.home.taskinfo.TaskInfoRoute
import com.mohammed.planity.ui.theme.gray400

// --- ONBOARDING NAVIGATION ---

object OnboardingDestinations {
    const val GET_STARTED = "get_started"
    const val AUTH_ROUTE = "auth_flow" // A route for the entire auth screen/flow
}
@Composable
fun OnboardingNavHost(
    navController: NavHostController,
    onOnboardingFinished: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestinations.GET_STARTED
    ) {
        composable(OnboardingDestinations.GET_STARTED) {
            GetStartedScreen(
                // Clicking the button now immediately signals that onboarding is finished
                onGetStartedClick = onOnboardingFinished
            )
        }
    }
}


// --- MAIN APP NAVIGATION ---

@Composable
fun AppNavHost(navController: NavHostController,onSignOut: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Home.route
    ) {
        composable(Destinations.Settings.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
            ) {
            SettingsScreenRoute(
                onNavigateBack = { navController.popBackStack() },
                onSignedOut = onSignOut // <-- Pass the callback down
            )
        }
        composable(Destinations.Home.route) {
            HomeScreenRoute(
                onSettingsClick = { navController.navigate(Destinations.Settings.route) },
                onAddCategoryClick = { navController.navigate(Destinations.CreateCategory.route) },
                onNavigateToTaskInfo = { taskId ->
                    navController.navigate(Destinations.TaskInfo.withArgs(taskId))
                }
            )
        }

        composable(
            route = Destinations.TaskInfo.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) {
            TaskInfoRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.Category.route) { // <-- CORRECTED: Was Destinations.Category
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



        dialog(Destinations.CreateTask.route) {
            CreateTaskScreenRoute(
                onDismiss = { navController.popBackStack() }
            )
        }

        dialog(Destinations.CreateCategory.route) {
            CreateCategoryRoute(
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}
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