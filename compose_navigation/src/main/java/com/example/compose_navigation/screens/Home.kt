package com.example.compose_navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose_navigation.NavRoutes

@Composable
fun Home(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    val onUserNameChange = { text: String -> userName = text}

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                title = "Enter your name",
                textState = userName,
                onTextChange = onUserNameChange)
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = {
                navController.navigate(NavRoutes.welcome.route + "/$userName")
            }
            ) {
                Text(text = "Register")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = textState,
        onValueChange = {onTextChange(it)},
        singleLine = true,
        label = { Text(text = title) },
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    )
}