package com.edxavier.vueloseaai.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class FlightData(
    var logo: String,
    var flight: String,
    var origin: String,
    var time: String,
    var status: String,
    var gate: String = ""
){
    @Composable
    fun statusColor(): Color{
            return MaterialTheme.colorScheme.onPrimaryContainer
    }
}