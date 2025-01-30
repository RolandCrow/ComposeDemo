package com.example.compose_navigation

sealed class NavRoutes(val route: String) {
    data object home: NavRoutes("home")
    data object welcome: NavRoutes("welcome")
    data object profile: NavRoutes("profile")
}