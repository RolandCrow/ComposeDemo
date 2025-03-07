package com.example.compose_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

// cold flow
class DemoViewModel: ViewModel() {

    // producer block
    val myFlow: Flow<Int> = flow{
        for(i in 0..9) {
            emit(i)
            delay(500)
        }
    }

    // intermediary
    val newFlow = myFlow.map {
        "Current value = $it"
    }

    // filter
    val newFlow2 = myFlow
        .filter { it % 2 == 0}
        .map {
            "Current value $it"
        }
    val newFlow3 = myFlow
        .transform {
            emit("Value = $it")
            delay(500)
            val doubled = it * 2
            emit("doubled value = $doubled")
        }

    fun doubleIt(value: Int) = flow {
        emit(value)
        delay(1000)
        emit(value + value)
    }
}

// hot flow
class DemoViewModel2: ViewModel() {
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    fun increaseValue() {
        _stateFlow.value += 1
    }
}

// shared flow
class DemoViewModel3: ViewModel() {
    private val _sharedFlow = MutableSharedFlow<Int>(
        replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()
    val subCount = _sharedFlow.subscriptionCount

    fun startSharedFlow() {
        viewModelScope.launch {
            for(i in 1..5) {
                _sharedFlow.emit(i)
                delay(2000)
            }
        }
    }
}