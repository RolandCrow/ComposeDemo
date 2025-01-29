package com.example.compose_view_models

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_view_models.ui.theme.ComposeDemoTheme
import com.example.compose_view_models.viewmodels.DemoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
}

@Composable
fun ScreenSetup(viewModel : DemoViewModel = viewModel()) {
    MainScreen(
        isFahrenheit = viewModel.isFahrenheit,
        result = viewModel.result,
        convertTemp = {viewModel.convertTemp(it)},
        switchChange = {viewModel.switchChange()},
        removeSpecialChars = {viewModel.removeSpecialChars(it)}
    )
}

@Composable
fun MainScreen(
    isFahrenheit: Boolean,
    result: String,
    convertTemp: (String) -> Unit,
    switchChange: () -> Unit,
    removeSpecialChars: (String) -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        var textState by remember { mutableStateOf("") }
        val onTextChanged = {text: String ->
            textState = removeSpecialChars(text)
            convertTemp(textState)
        }
        Text(
            text = "Temp Converter",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        InputRow(
            isFahrenheit = isFahrenheit,
            textState = textState,
            onTextChanged =  onTextChanged,
            switchChange = switchChange,
            convertTemp = convertTemp
        )
        Crossfade(
            targetState = !isFahrenheit,
            animationSpec = tween(2000),
            label = "crossFade"
        ) { visible ->
            when(visible) {
                true -> Text(
                    text = "$result \u2109",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(28.dp)
                )
                false -> Text(
                    text = "$result \u2103",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputRow(
    isFahrenheit: Boolean,
    textState: String,
    onTextChanged: (String) -> Unit,
    switchChange: () -> Unit,
    convertTemp: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = isFahrenheit,
            onCheckedChange = {switchChange(); convertTemp(textState)}
        )
        OutlinedTextField(
            value = textState,
            onValueChange = {onTextChanged(it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            label = {Text("Enter Temperature")},
            modifier = Modifier.padding(10.dp),
            textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_ac_unit_24),
                    contentDescription = "frost",
                    modifier = Modifier.size(40.dp)
                )
            }
        )
        Crossfade(
            targetState = isFahrenheit,
            animationSpec = tween(2000), label = "crossfade"
        ) { visible ->
            when(visible) {
                true -> Text(
                    "\u2109", style = MaterialTheme.typography.headlineSmall
                )
                false -> Text(
                    "\u2103", style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(model: DemoViewModel = viewModel()) {
    ComposeDemoTheme {
        MainScreen(
            isFahrenheit = model.isFahrenheit,
            result = model.result,
            convertTemp = {model.convertTemp(it)},
            switchChange = {model.switchChange()},
            removeSpecialChars = {model.removeSpecialChars(it)}
        )
    }
}
