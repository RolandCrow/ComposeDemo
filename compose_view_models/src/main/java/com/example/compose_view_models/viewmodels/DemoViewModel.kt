package com.example.compose_view_models.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class DemoViewModel: ViewModel() {
    var isFahrenheit by mutableStateOf(true)
    var result by mutableStateOf("")

    fun convertTemp(temp: String) {
        if(temp == "") {
            result = ""; return
        }
        result = try {
            val tempFloat = temp.toFloat()
            if(isFahrenheit) {
                BigDecimal((tempFloat - 32) * 0.5556).setScale(5, RoundingMode.HALF_EVEN).toString()
            } else {
                BigDecimal((tempFloat * 1.8) + 32).setScale(5,RoundingMode.HALF_EVEN).toString()
            }
        } catch (e: Exception) {
            "Invalid Entry"
        }
    }

    fun switchChange() {
        isFahrenheit = !isFahrenheit
    }

    fun removeSpecialChars(text:String): String {
        var textTemp = text
        textTemp = text.replace(",",".")
        return if(textTemp.matches(Regex("^-?\\d*(\\d+[.]\\d*)?\$"))) textTemp
        else if(textTemp != "") Regex(".*(?!\$)").find(text)!!.value
        else ""
    }

}