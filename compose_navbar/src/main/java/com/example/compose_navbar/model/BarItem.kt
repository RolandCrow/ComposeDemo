package com.example.compose_navbar.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)
