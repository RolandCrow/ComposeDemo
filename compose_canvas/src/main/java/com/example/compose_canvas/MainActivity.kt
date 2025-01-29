package com.example.compose_canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_canvas.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.internal.PrepareOp
import kotlin.math.PI
import kotlin.math.sin

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
    Box {
        Box(Modifier.width(303.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier
                    .background(Color.Yellow),
                contentPadding = PaddingValues(1.dp,1.dp,1.dp,1.dp)
            ) {
                items(12) { num ->
                    Box(
                        Modifier.background(Color.Yellow)
                    ) {
                        when(num) {
                            0 -> DrawLine()
                            1 -> DrawRect()
                            2 -> DrawCircle()
                            3 -> DrawOval()
                            4 -> GradientFill()
                            5 -> RadialFill()
                            6 -> ShadowCircle()
                            7 -> DrawArc()
                            8 -> DrawPath()
                            9 -> DrawPoints()
                            10 -> DrawImage()
                            11 -> DrawText()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawLine() {
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .background(Color.Gray)
    ) {
        val height = size.height
        val width = size.width

        drawLine(
            start = Offset(x = 0f,y = 0f),
            end = Offset(x = width, y = height),
            color = Color.Blue,
            strokeWidth = 16f,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(30f,10f,10f,10f), phase = 0f
            )
        )
    }
}

@Composable
fun DrawRect() {
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .background(Color.DarkGray)
    ) {
        val sizeRectangle = Size(width = size.width/2, height = 100.dp.toPx())
        inset(100f,200f) {
            rotate(45f) {
                drawRoundRect(
                    color = Color.Red,
                    size = sizeRectangle,
                    topLeft = Offset(
                        x = (size.width - sizeRectangle.width) / 2,
                        y = (size.height - sizeRectangle.height) / 2
                    ),
                    style = Stroke(width = 6.dp.toPx()),
                    cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
                )
            }
        }
    }
}

@Composable
fun DrawCircle() {
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .background(Color.White)
    ) {
        drawCircle(
            color = Color.Magenta,
            center = center,
            radius = 50.dp.toPx()
        )
    }
}

@Composable
fun DrawOval() {
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .background(Color.LightGray)
    ) {
        drawOval(
            color = Color.Green,
            topLeft = Offset(x=25.dp.toPx(),y = 90.dp.toPx()),
            size = Size(
                width = size.width - 50.dp.toPx(),
                height = size.height / 2 - 50.dp.toPx()
            ),
            style = Stroke(width = 12.dp.toPx())
        )
    }
}

@Composable
fun GradientFill() {
    Canvas(modifier = Modifier.size(150.dp)) {
        val canvasSize = size
        val colorList: List<Color> =
            listOf(Color.Red,Color.Yellow,Color.Green,Color.Blue,Color.Magenta)
        val brush = Brush.horizontalGradient(
            colors = colorList,
            startX = 0f,
            endX = 150.dp.toPx(),
            tileMode = TileMode.Repeated
        )
        drawRect(
            brush = brush,
            size = canvasSize
        )
    }
}

@Composable
fun RadialFill() {
    Canvas(Modifier.size(150.dp)) {
        val radius = 75.dp.toPx()
        val colorList: List<Color> =
            listOf(Color.Red,Color.Yellow,Color.Green,Color.Blue,Color.Magenta)
        val brush = Brush.radialGradient(
            colors = colorList,
            center = center,
            radius = radius,
            tileMode = TileMode.Repeated
        )
        drawCircle(
            brush, radius, center
        )
    }
}

@Composable
fun ShadowCircle() {
    Canvas(Modifier.size(150.dp)) {
        val radius = 75.dp.toPx()
        val colorList: List<Color> = listOf(Color.Blue,Color.Black)
        val brush = Brush.linearGradient(
            colors = colorList,
            start = Offset(0f,0f),
            end = Offset(150.dp.toPx(),150.dp.toPx()),
            tileMode = TileMode.Repeated
        )
        drawCircle(
            brush, radius
        )
    }
}

@Composable
fun DrawArc() {
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .background(Color.Gray)) {
        drawArc(
            color = Color.Red,
            startAngle = 315f,
            sweepAngle = 90f,
            useCenter = true,
            size = Size(150.dp.toPx(),150.dp.toPx())
        )
    }
}

@Composable
fun DrawPath() {
    Canvas(
        Modifier
            .size(150.dp)
            .background(Color.White)) {
        val path = Path().apply {
            moveTo(0f,0f)
            quadraticBezierTo(
                25.dp.toPx(),
                100.dp.toPx(),
                150.dp.toPx(),
                150.dp.toPx()
            )
            lineTo(135.dp.toPx(),50.dp.toPx())
            quadraticBezierTo(
                30.dp.toPx(),
                40.dp.toPx(),
                0.dp.toPx(),
                0.dp.toPx()
            )
        }
        drawPath(
            path = path,
            color= Color.DarkGray
        )
    }
}

@Composable
fun DrawPoints() {
    Canvas(
        Modifier
            .size(150.dp)
            .background(Color.Cyan)
    ) {
        val height = size.height
        val width = size.width
        val points = mutableListOf<Offset>()
        (0..size.width.toInt()).forEach { x->
            val y = (sin(x*(2f*PI/width)) * (height/2) + (height/2)).toFloat()
            points.add(Offset(x.toFloat(),y))
        }
        drawPoints(
            points = points,
            strokeWidth = 3f,
            pointMode = PointMode.Points,
            color = Color.Blue
        )
    }
}


@Composable
fun DrawImage() {
    val image = ImageBitmap.imageResource(id = R.drawable.vacation)

    Canvas(Modifier.size(150.dp)) {
        val scaleRation = 150.dp.toPx()/image.width
        scale(
            scale = scaleRation,
            pivot = Offset(0f,0f)
        ) {
            drawImage(
                image = image
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawText() {
    val colorList: List<Color> =
        listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta)
    val textMeasurer = rememberTextMeasurer()

    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                brush = Brush.verticalGradient(colorList)
            )
        ) {
            append("Hello annotated string")
        }
    }

    Canvas(
        Modifier
            .size(150.dp)
            .background(Color.White)
    ) {
        drawRect(
            brush = Brush.horizontalGradient(colorList)
        )
        drawText(textMeasurer = textMeasurer, text = annotatedText)
    }
}

@Preview(showBackground = true)
@Composable
fun  MainScreenPreview() {
    ComposeDemoTheme {
        MainScreen()
    }
}