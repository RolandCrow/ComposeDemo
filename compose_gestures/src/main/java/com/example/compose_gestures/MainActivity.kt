package com.example.compose_gestures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compose_gestures.ui.theme.ComposeDemoTheme
import kotlin.math.roundToInt

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
    Column(Modifier.fillMaxSize()) {
        Row {
            ClickDemo()
            Spacer(Modifier.width(10.dp))
            TapPressDemo()
            Spacer(Modifier.width(10.dp))
            DragDemo()
        }
        Spacer(Modifier.height(10.dp))
        Row {
            PointerInputDrag()
            Spacer(Modifier.width(10.dp))
            ScrollableModifier()
            Spacer(Modifier.width(10.dp))
            ScrollModifiers()
        }
        Spacer(Modifier.height(10.dp))
        MultiTouchDemo()
    }
}

@Composable
fun ClickDemo() {
    var colorState by remember { mutableStateOf(true) }
    var bgColor by remember { mutableStateOf(Color.Blue) }

    val clickHandler = {
        colorState = !colorState
        bgColor = if (colorState) Color.Blue else Color.DarkGray
    }

    Box(
        Modifier
            .clickable { clickHandler() }
            .background(bgColor)
            .size(100.dp)
    )
}

@Composable
fun TapPressDemo() {
    var textState by remember { mutableStateOf("Waiting...") }
    val tapHandler = { status: String ->
        textState = status
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier
                .padding(10.dp)
                .background(Color.Red)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {tapHandler("onPress detected !")},
                        onDoubleTap = {tapHandler("onDoubleTap detected !")},
                        onLongPress = {tapHandler("onLongPress detected !")},
                        onTap = {tapHandler("onTap detected !")}
                    )
                }
        )
        Spacer(Modifier.width(10.dp))
        Text(text = textState)
    }
}

@Composable
fun DragDemo() {
    Box(modifier = Modifier.fillMaxWidth()) {
        var xOffset by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .offset { IntOffset(xOffset.roundToInt(), 0) }
                .size(100.dp)
                .background(Color.Cyan)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { distance ->
                        xOffset += distance
                    }
                )
        )
    }
}

@Composable
fun PointerInputDrag() {
    Box {
        var xOffset by remember { mutableStateOf(0f) }
        var yOffset by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset {IntOffset(xOffset.roundToInt(), yOffset.roundToInt())}
                .background(Color.Magenta)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, distance ->
                        xOffset += distance.x
                        yOffset += distance.y
                    }
                }
        )
    }
}

@Composable
fun ScrollableModifier() {
    var offset by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .scrollable(
                orientation =  Orientation.Vertical,
                state = rememberScrollableState { distance ->
                    offset += distance
                    distance
                }
            )
    ) {
        Box(
            Modifier
                .size(90.dp)
                .offset {IntOffset(0, offset.roundToInt())}
                .background(Color.DarkGray)
        )
    }
}

@Composable
fun ScrollModifiers() {
    val image = ImageBitmap.imageResource(R.drawable.vacation)
    Box(
        Modifier
            .size(150.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier.size(360.dp,270.dp)
        ) {
            drawImage(
                image = image,
                topLeft = Offset(0f,0f)
            )
        }
    }
}

@Composable
fun MultiTouchDemo() {
    var scale by remember { mutableStateOf(1f) }
    var angle by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { scaleChange, offsetChange, rotationChange ->
        scale *= scaleChange
        angle += rotationChange
        offset += offsetChange
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            Modifier.graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = offset.x,
                translationX = offset.x,
                translationY = offset.y
            )
                .transformable(state = state)
                .background(Color.Yellow)
                .size(100.dp)
        )
    }
}