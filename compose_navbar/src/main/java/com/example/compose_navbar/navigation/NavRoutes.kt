package com.example.compose_navbar.navigation

sealed class NavRoutes(val route: String) {
    data object home: NavRoutes("home")
    data object contacts: NavRoutes("contacts")
    data object favorites: NavRoutes("favorites")
}