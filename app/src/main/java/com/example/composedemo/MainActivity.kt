package com.example.composedemo

import android.os.Bundle
import android.widget.CheckBox
import androidx.compose.material3.Checkbox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    var linearSelected by remember {
        mutableStateOf(true)
    }

    var imageSelected by remember {
        mutableStateOf(true)
    }
    val onLinearClick = {value: Boolean -> linearSelected  = value}
    val onTitleClick = {value: Boolean -> imageSelected = value}

    ScreenContent(
        linearSelected = linearSelected,
        imageSelected = imageSelected,
        onTitleClick = onTitleClick,
        onLinearClick = onLinearClick,
        titleContent = {
            if(imageSelected) {
                TitleImage(drawing = R.drawable.ic_launcher_foreground)
            } else {
                Text(
                    text = "Downloading",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(30.dp)
                )
            }
        },
        progressContent = {
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .padding(all = 10.dp)
            ) {
                if(linearSelected) {
                    LinearProgressIndicator(
                        Modifier.height(40.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        Modifier.size(200.dp),
                        strokeWidth = 18.dp
                    )
                }
            }
        }
    )
}

@Composable
fun ScreenContent(
    linearSelected: Boolean,
    imageSelected: Boolean,
    onTitleClick: (Boolean) -> Unit,
    onLinearClick: (Boolean) -> Unit,
    titleContent: @Composable () -> Unit,
    progressContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        titleContent()
        progressContent()

        Image(
            contentScale = ContentScale.Fit,
            contentDescription = "",
            painter = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(10.dp))
        )
        CheckBoxes(
            linearSelected = linearSelected,
            imageSelected = imageSelected,
            onTitleClick = onTitleClick,
            onLinearClick = onLinearClick
        )
    }
}

@Composable
fun CascadeLayout(
    modifier: Modifier = Modifier,
    spacing: Int = 0,
    content: @Composable () -> Unit
) {

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        var indent = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            var yCoord = 0
            val placeable = measurables.map {measurable ->
                measurable.measure(constraints)
            }

            placeable.forEach { placeables ->
                placeables.placeRelative(
                    x = indent,
                    y = yCoord
                )
                indent += placeables.width + spacing
                yCoord += placeables.height + spacing
            }
        }
    }
}

@Composable
fun TitleImage(drawing: Int) {
    Image(painter = painterResource(
        id = drawing),
        contentDescription = "title image",
        modifier = Modifier.size(150.dp)
    )
}

@Composable
fun CheckBoxes(
    linearSelected: Boolean,
    imageSelected: Boolean,
    onTitleClick: (Boolean) -> Unit,
    onLinearClick: (Boolean) -> Unit
) {
    Row(
        Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = imageSelected,
            onCheckedChange = onTitleClick
        )
        Text("Image Title")

        Spacer(modifier = Modifier.width(20.dp))

        Checkbox(checked = linearSelected,
            onCheckedChange = onLinearClick
        )
    }
}

data class ItemProperties(
    val color: Color,
    val width: Dp,
    val height: Dp,
)

@Composable
fun ColumnList() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                modifier = Modifier
                    .weight(0.5F)
                    .padding(2.dp)
            ) {
                Text(text = "Top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(2.dp)
            ) {
                Text(text = "End")
            }
        }

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            repeat(500) {
                Text(
                    "List Item $it",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    text: String, onTextChange: (String) -> Unit
) {
    TextField(value = text, onValueChange = onTextChange)
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextCell(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 150
) {
    val calModifier = Modifier
        .padding(4.dp)
        .border(
            width = 4.dp,
            shape = RectangleShape,
            brush = Brush.linearGradient(colors = listOf(Color.Black, Color.Red, Color.Cyan))
        )

    Surface {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Green,Color.Black))
                )
            ) {
                append(text)
            }
        },
            calModifier.then(modifier),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun BrushStyle() {
    val colorList: List<Color> = listOf(Color.Red,Color.Blue,Color.Magenta,Color.Yellow,Color.Green,
        Color.Red)

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 70.sp,
                    brush = Brush.linearGradient(colors = colorList)
                )
            )  {
                append("COMPOSE!")
            }
        }
    )
}

@Composable
fun ParaString() {
    Text(
        buildAnnotatedString {
            append("\nThis is some text that doesn't have any style applied to it.\n")
            withStyle(style = ParagraphStyle(
                lineHeight = 30.sp,
                textIndent = TextIndent(
                    firstLine = 60.sp,
                    restLine = 25.sp
                )
            )
            ) {
                append(
                    "This is some text that is indented more on the first lines than the rest of " +
                            "the lines. It also has an increased line height.\n"
                )
            }
        }
    )
}

@Composable
fun SpanString() {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            ) {
                append("T")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Green
                )
            ) {
                append("his")
            }

            append(" is ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Blue
                )
            ) {
                append("great")
            }
        }
    )
}

