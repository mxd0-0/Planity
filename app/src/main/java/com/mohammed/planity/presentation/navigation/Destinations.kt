package com.mohammed.planity.presentation.navigation


/**
 * A sealed class that defines all possible navigation destinations in the app.
 * This is the single source of truth for routes.
 */
sealed class Destinations(val route: String) {
    // Destinations accessible from the Bottom Navigation Bar
    object Home : Destinations("home")
    object Calendar : Destinations("calendar")
    object Category : Destinations("category")
    object Graph : Destinations("graph")

    // Other destinations not in the Bottom Navigation Bar
    object Settings : Destinations("settings")
    object CreateTask : Destinations("create_task")
    object CreateCategory : Destinations("create_category")
    object TaskInfo : Destinations("task_info/{taskId}") {
        // Helper function to create the route with arguments
        fun withArgs(taskId: String): String {
            return "task_info/$taskId"
        }
    }
    // Add other screens like TaskDetail, etc. here
}