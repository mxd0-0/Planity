package com.mohammed.planity.presentation.navigation


import com.mohammed.planity.R


// This data class defines the properties for each item in the bottom bar.
// It should live in a file accessible by this composable, e.g., BottomNavItem.kt
data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: Int, // Use @DrawableRes Int for resources
    val unSelectedIcon: Int
)

// This list defines the items that will appear in the bottom bar.
val bottomNavItems = listOf(
    BottomNavItem(
        route = Destinations.Home.route,
        title = "Home",
        selectedIcon = R.drawable.home_selected, // Replace with your actual drawable resources
        unSelectedIcon = R.drawable.home
    ),
    BottomNavItem(
        route = Destinations.Category.route,
        title = "Category",
        selectedIcon = R.drawable.category_selected,
        unSelectedIcon = R.drawable.category
    ),
    BottomNavItem(
        route = Destinations.Graph.route,
        title = "Graph",
        selectedIcon = R.drawable.graph_selected,
        unSelectedIcon = R.drawable.graph
    )
    // Add Calendar here if you re-introduce it
)


