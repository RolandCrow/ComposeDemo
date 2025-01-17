package com.example.coroutines_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.coroutines_compose.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    var counter by remember { mutableStateOf(0) }
    val increaseCounter = {counter+=1}

    val channel = Channel<Int>()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch(Dispatchers.Main) { performSlowTask2() }
    }
    SideEffect {
        coroutineScope.launch { performSlowTask3() }
    }
    Column() {
        MyButton(increaseCounter = increaseCounter, coroutineScope = coroutineScope)
        MyText(counter = counter)
        MyButton2(coroutineScope = coroutineScope, channel = channel)
    }
}

@Composable
fun MyButton(increaseCounter: ()-> Unit, coroutineScope: CoroutineScope) {
    Button(onClick = {
        coroutineScope.launch { performSlowTask() }
        increaseCounter()
    }) {
        Text(text = "Click me")
    }
}

suspend fun performTask1(channel: Channel<Int>) {
    (1..6).forEach {
        channel.send(it)
    }
}

suspend fun performTask2(channel: Channel<Int>) {
    repeat(6) {
        println("Received ${channel.receive()}")
    }
}


@Composable
fun MyButton2(coroutineScope: CoroutineScope, channel: Channel<Int>) {
    Button(onClick = {
        coroutineScope.launch {
            coroutineScope.launch(Dispatchers.Main) { performTask1(channel) }
            coroutineScope.launch(Dispatchers.Main) { performTask2(channel) }
        }
    }) {
        Text(text = "Click me too")
    }
}

@Composable
fun MyText(counter: Int) {
    Text(text = counter.toString())
}

suspend fun performSlowTask() {
    println("performSlowTask before")
    delay(5000)
    println("performSlowTask after")
}

suspend fun performSlowTask2() {
    println("performSlowTask 2 before")
    delay(5000)
    println("performSlowTask 2 after")
}

suspend fun performSlowTask3() {
    println("performSlowTask 3 before")
    delay(5000)
    println("performSlowTask 3 after")
}
