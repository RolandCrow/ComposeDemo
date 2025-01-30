package com.example.compose_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose_navigation.screens.Home
import com.example.compose_navigation.screens.Profile
import com.example.compose_navigation.screens.Welcome
import com.example.compose_navigation.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.home.route
    ) {
        composable(NavRoutes.home.route) {
            Home(navController = navController)
        }
        composable(NavRoutes.welcome.route + "/{userName}") { navBackStackEntry ->
            val userName = navBackStackEntry.arguments?.getString("userName")
            Welcome(navController = navController, userName)
        }
        composable(NavRoutes.profile.route) {
            Profile(navController = navController)
        }
    }
}

