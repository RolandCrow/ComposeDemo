package com.example.compose_navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose_navigation.NavRoutes

@Composable
fun Welcome(navController: NavController, userName: String?) {
    val un = userName
    un ?: ""
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
               text = "Welcome $userName",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {
                    navController.navigate(NavRoutes.profile.route) {
                        popUpTo(NavRoutes.home.route)
                    }
                }
            ) {
                Text(text = "Set up your profile")
            }
        }
    }
}