package com.example.animation_state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.animation_state.data.BoxColor
import com.example.animation_state.data.BoxPosition
import com.example.animation_state.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        RotationDemo()
        ColorChangeDemo()
        MotionDemo()
        TransitionDemo()
    }
}

@Composable
fun RotationDemo() {
    var angleValue by remember { mutableStateOf(0f) }
    val angle by animateFloatAsState(
        targetValue = angleValue,
        animationSpec = tween(durationMillis = 2500),
        label = "rotation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.propeller),
            contentDescription = "fan",
            modifier = Modifier
                .rotate(angle)
                .padding(10.dp)
                .size(150.dp)
        )
        Button(
            onClick = {angleValue += 360f},
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Rotate Propeller")
        }
    }
}

@Composable
fun ColorChangeDemo() {
    var colorState by remember { mutableStateOf(BoxColor.Red) }

    val animateColor: Color by animateColorAsState(
        targetValue = when (colorState) {
            BoxColor.Red ->Color.Red
            BoxColor.Magenta -> Color.Magenta
        },
        animationSpec = tween(4500),
        label = "color changing"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(150.dp)
                .background(animateColor)
        )
        Button(
            onClick = {
                colorState = when(colorState) {
                    BoxColor.Red -> BoxColor.Magenta
                    BoxColor.Magenta -> BoxColor.Red
                }
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Change color")
        }
    }

}

@Composable
fun MotionDemo() {
    var boxState by remember { mutableStateOf(BoxPosition.Start) }
    var boxSideLength = 70.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val animateOffset: Dp by animateDpAsState(
        targetValue = when(boxState) {
            BoxPosition.Start -> 0.dp
            BoxPosition.End -> screenWidth - boxSideLength
        },
        animationSpec = keyframes {
            delayMillis = 1000
            100.dp.at(10).with(LinearEasing)
            110.dp.at(500).with(FastOutSlowInEasing)
            200.dp.at(700).with(LinearOutSlowInEasing)
        },
        label = "object moving"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .offset(x = animateOffset, y = 20.dp)
                .size(boxSideLength)
                .background(Color.DarkGray)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                boxState = when(boxState) {
                    BoxPosition.Start -> BoxPosition.End
                    BoxPosition.End -> BoxPosition.Start
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Move Box")
        }
    }
}

@Composable
fun TransitionDemo() {
    var boxState by remember { mutableStateOf(BoxPosition.Start) }
    var screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val transition = updateTransition(
        targetState = boxState,
        label = "Color and Motion"
    )
    val animatedColor: Color by transition.animateColor(
        transitionSpec = {
            tween(4000)
        },
        label = "colorAnimation"
    ) { state ->
        when (state) {
            BoxPosition.Start -> Color.Red
            BoxPosition.End -> Color.Magenta
        }
    }
    val animatedOffset: Dp by transition.animateDp(
        transitionSpec = {
            tween(4000)
        },
        label = "offesetAnimation"
    ) { state ->
        when (state) {
            BoxPosition.Start -> 0.dp
            BoxPosition.End -> screenWidth - 70.dp
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .offset(x = animatedOffset, y = 20.dp)
                .size(70.dp)
                .background(animatedColor)
        )
        Spacer(Modifier.height(50.dp))
        Button(
            onClick = {
                boxState = when (boxState) {
                    BoxPosition.Start -> BoxPosition.End
                    BoxPosition.End -> BoxPosition.Start
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Move + change color")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RotationDemoPreview() {
    MainScreen()
}