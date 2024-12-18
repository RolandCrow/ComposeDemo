package com.example.composedemo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.compose.material3.Checkbox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
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
import com.example.composedemo.ui.theme.data.BoxColor
import com.example.composedemo.ui.theme.data.BoxProperties
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun ColorChangeDemo() {
    var colorState by remember {
        mutableStateOf(BoxColor.Red)
    }

    val animatedColor by animateColorAsState(
        targetValue = when(colorState) {
            BoxColor.Red -> Color.Magenta
            BoxColor.Magenta -> Color.Red
        },
        animationSpec = tween(4500),
        label = "Color Change"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(200.dp)
                .background(animatedColor)
        )

        Button(
            onClick = {
                colorState = when(colorState) {
                    BoxColor.Red -> BoxColor.Magenta
                    BoxColor.Magenta -> BoxColor.Red
                }
            },
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Change Color")
        }
    }
}

@Composable
fun RotationDemo() {
    var rotated by remember {
        mutableStateOf(false)
    }

    val angle by animateFloatAsState(
        targetValue = if(rotated) 360f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = LinearEasing),
        label = "Rotate"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.typescript),
            contentDescription = "fan",
            modifier = Modifier
                .rotate(angle)
                .padding(10.dp)
                .size(300.dp)
        )

        Button(
            onClick = {rotated = !rotated},
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Rotate Propeller")
        }
    }
}

@Composable
fun RotationPreview() {
    RotationDemo()
    ColorChangeDemo()
}

@Composable
fun AnimationVisibilityPreview() {
    var boxVisible by remember {
        mutableStateOf(true)
    }

    val onClick = { newState: Boolean ->
        boxVisible = newState
    }

    val state = remember {
        MutableTransitionState(false)
    }

    Column(
        modifier = Modifier
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Crossfade(
                targetState = boxVisible,
                animationSpec = tween(5000),
                label = "crossFade"
            ) { visible ->
                when(visible) {
                    true -> CustomButton(
                        text = "Hide",
                        targetState = false,
                        onClick = onClick,
                        bgColor = Color.Red
                    )
                    else -> CustomButton(
                        text = "Show",
                        targetState = true,
                        onClick = onClick,
                        bgColor = Color.Magenta
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        AnimatedVisibility(
            visible = boxVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 5000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 5000)) +
                    shrinkVertically(animationSpec = tween(durationMillis = 5000)),
        ) {
            Box(
                modifier = Modifier
                    .size(height = 200.dp, width = 200.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    targetState: Boolean,
    onClick: (Boolean) -> Unit,
    bgColor: Color = Color.Blue
) {
    Button(
        onClick = {onClick(targetState)},
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun GridItem(properties: BoxProperties) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .width(properties.width)
            .clip(RoundedCornerShape(0.dp))
            .background(properties.color)
    )
}

@Composable
fun CoverPager() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaggeredPreview() {
    val items = (1..50).map {
        BoxProperties(
            height = Random.nextInt(50,200).dp,
            width = Random.nextInt(50,200).dp,
            color = Color(
                Random.nextInt(255),
                Random.nextInt(255),
                Random.nextInt(255),
                255
            )
        )
    }
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { boxProperties ->
            GridItem(properties = boxProperties)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cars(cars: Array<out String>) {
    val context = LocalContext.current
    val groupCars = cars.groupBy { it.substringBefore(' ') }
    val onCarItemClick = {text: String ->
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val displayButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 5
        }
    }

    Box {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            groupCars.forEach { (manufacture, models) ->
                stickyHeader {
                    Text(
                        text = manufacture,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Gray)
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
                }
                items(models) { model ->
                    CarListItem(item = model, onCarItemClick)
                }
            }
        }

        AnimatedVisibility(
            visible = displayButton.value,
            Modifier.align(Alignment.BottomCenter)
        ) {
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                border = BorderStroke(1.dp,Color.Gray),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.DarkGray
                ),
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = "Top")
            }
        }
    }
}


@Composable
fun ItemPreview() {
    CarListItem(item = "Buick Roadmaster") {}
}

@Composable
fun CarListItem(
    item: String,
    onCarItemClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCarItemClick(item) }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            ImageLoader(item = item)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                item,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ImageLoader(item: String) {
    val url =
        "https://www.ebookfrenzy.com/book_examples/car_logos/" + item.substringBefore(" ") +
                "_logo.png"
    Image(
        painter = painterResource(id = R.drawable.girl),
        contentDescription = "car image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .size(75.dp)
    )
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
            painter = painterResource(id = R.drawable.girl),
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

