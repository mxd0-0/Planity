package com.mohammed.planity.presentation.navigation


import androidx.annotation.DrawableRes
import com.mohammed.planity.R

data class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int
)

// The list of items to be displayed in the bottom navigation bar
val bottomNavItems = listOf(
    BottomNavItem(
        route = Destinations.Home.route,
        title = "Home",
        selectedIcon = R.drawable.home_selected,
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
)
