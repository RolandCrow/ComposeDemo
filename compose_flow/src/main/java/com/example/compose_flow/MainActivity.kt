package com.example.compose_flow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_flow.ui.theme.ComposeDemoTheme
import com.example.compose_flow.viewmodel.DemoViewModel
import com.example.compose_flow.viewmodel.DemoViewModel2
import com.example.compose_flow.viewmodel.DemoViewModel3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.zip
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenSetup()
                }
            }
        }
    }

    @Composable
    fun ScreenSetup(
        viewModel: DemoViewModel = viewModel(),
        viewModel2: DemoViewModel2 = viewModel(),
        viewModel3: DemoViewModel3 = viewModel()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainScreen(viewModel.newFlow3)
            MainScreen2(viewModel.newFlow3)
            MainScreen3(viewModel.newFlow3)
            MainScreen4(viewModel.myFlow)
            MainScreen5(viewModel)
            MainScreen6()
            MainScreen7(viewModel2)
            MainScreen8(viewModel3)
        }
    }

    @Composable
    fun MainScreen(flow: Flow<String>) {
        val count by flow.collectAsState(initial = "Current value=")
        Text(text = count, style = TextStyle(fontSize = 40.sp))
    }

    @Composable
    fun MainScreen2(flow: Flow<String>) {
        var count by remember { mutableStateOf("Current value=") }

        LaunchedEffect(Unit) {
            try {
                flow.collect {
                    count = it
                }
            } finally {
                count = "Flow stream ended!"
            }
        }
        Text(text = count, style = TextStyle(fontSize = 40.sp))
    }

    @Composable
    fun MainScreen3(flow: Flow<String>) {
        var count by remember { mutableStateOf("Current value=") }

        LaunchedEffect(Unit) {
            val elapsedTime = measureTimeMillis {
                flow
                    .buffer()
                    .collect {
                        count = it
                        delay(1000)
                    }
            }
            count = "Duration =$elapsedTime"
        }
        Text(text = count, style = TextStyle(fontSize = 40.sp))
    }

    @Composable
    fun MainScreen4(flow: Flow<Int>) {
        var count by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            flow.reduce {accumulator, value ->
                count = accumulator
                accumulator + value
            }
        }
        Text(text = "$count", style = TextStyle(fontSize = 40.sp))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    fun MainScreen5(viewModel: DemoViewModel = viewModel()) {
        var count by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            viewModel.myFlow
                .flatMapMerge { viewModel.doubleIt(it) }
                .collect {
                    count = it
                    println("count = $it")
                }
        }
        Text(text = "$count", style = TextStyle(fontSize = 40.sp))
    }

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    fun MainScreen6() {
        // flow combinations
        var count by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            val flow1 = (1..5).asFlow().onEach { delay(1000) }
            val flow2 = flowOf("one","two","three","four").onEach { delay(1500) }
            flow1.zip(flow2) {value,string -> "$value, $string" }
                .collect{ count = it}
        }
        Text(text = count, style= TextStyle(fontSize = 40.sp))
    }

    @Composable
    fun MainScreen7(viewModel: DemoViewModel2) {
        // state flow
        val count by viewModel.stateFlow.collectAsState()

        Text(text = "$count", style = TextStyle(fontSize = 40.sp))
        Button(onClick = {viewModel.increaseValue()}) {
            Text(text = "Click state Flow")
        }
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun MainScreen8(viewModel:DemoViewModel3) {
        // shared flow
        val count by viewModel.sharedFlow.collectAsState(initial = 0)

        Text(text = "$count", style = TextStyle(fontSize = 40.sp))
        Button(onClick = {viewModel.startSharedFlow()}) {
            Text(text = "Click shared flow")
        }
        Text(text = "subscribers count = ${viewModel.subCount.value}")
    }

    @Preview
    @Composable
    fun ScreenSetupPreview() {
        ComposeDemoTheme {
            ScreenSetup(viewModel())
        }
    }
}
