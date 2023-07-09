package com.edxavier.vueloseaai.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.edxavier.vueloseaai.ui.theme.*

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
        return when (status) {
            "Confirmado", "Abordando" -> MaterialTheme.colorScheme.confirmed
            "Arribó", "Despegó" -> MaterialTheme.colorScheme.arrived
            "A Tiempo" -> MaterialTheme.colorScheme.on_time
            "Cancelado" -> MaterialTheme.colorScheme.canceled
            "Demorado" -> MaterialTheme.colorScheme.delayed
            else -> MaterialTheme.colorScheme.onPrimaryContainer
        }
    }
}