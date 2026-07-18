package com.edxavier.vueloseaai.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.edxavier.vueloseaai.ui.theme.LocalFlightStatusColors

class FlightData(
    var logo: String,
    var flight: String,
    var origin: String,
    var time: String,
    var status: String,
    var gate: String = ""
) {
    @Composable
    fun statusColor(): Color {
        val colors = LocalFlightStatusColors.current
        return when (status) {
            "Confirmado", "Abordando" -> colors.confirmed
            "Arribó", "Despegó" -> colors.arrived
            "A Tiempo" -> colors.onTime
            "Cancelado" -> colors.canceled
            "Demorado" -> colors.delayed
            else -> MaterialTheme.colorScheme.onSurface
        }
    }

    @Composable
    fun statusBgColor(): Color {
        val colors = LocalFlightStatusColors.current
        return when (status) {
            "Confirmado", "Abordando" -> colors.confirmedBg
            "Arribó", "Despegó" -> colors.arrivedBg
            "A Tiempo" -> colors.onTimeBg
            "Cancelado" -> colors.canceledBg
            "Demorado" -> colors.delayedBg
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    }
}
