package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.edxavier.vueloseaai.data.FlightsViewModel

private val iataToICAO = mapOf(
    "AA" to "AAL",   // American Airlines
    "AV" to "AVA",   // Avianca
    "CM" to "CMP",   // Copa Airlines
    "UA" to "UAL",   // United Airlines
    "NK" to "NKS",   // Spirit Airlines
    "DL" to "DAL",   // Delta Air Lines
    "AM" to "AMX",   // Aeromexico
    "B6" to "JBU",   // JetBlue
    "LA" to "LAN",   // LATAM
    "TA" to "TAI",   // TACA/Avianca
    "LR" to "LRC",   // LACSA/Copa
    "WN" to "SWA",   // Southwest
    "RZ" to "LRS",   // Sansa Airlines
    "NI" to "NIS",   // La Costeña (Nicaragua)
    "LC" to "NIS",   // La Costeña (alt)
    "V0" to "VCV",   // Conviasa (Venezuela)
    "DO" to "SHH",   // Sky High Aviation (Dominican)
    "AG" to "ARU",   // Aruba Airlines
    "4O" to "AIJ",   // Interjet (Mexico)
    "Q6" to "VOC",   // Volaris Costa Rica
    "Y4" to "VOI",   // Volaris (Mexico)
    "H2" to "SKU",   // Sky Airline (Chile)
)

private fun flightIdToICAO(flightId: String): String {
    if (flightId.length < 3) return flightId
    val letters = flightId.take(3)
    if (letters.all { it.isLetter() && it.isUpperCase() }) {
        return flightId
    }
    val iata = flightId.take(2)
    val number = flightId.drop(2)
    val icao = iataToICAO[iata]
    return if (icao != null) "$icao$number" else flightId
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternationalDetails(
    viewModel: FlightsViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val flightId = viewModel.flightId

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = if (flightId.isNotEmpty()) flightId else "Detalles",
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        )

        if (flightId.isNotEmpty()) {
            val fawareId = flightIdToICAO(flightId)
            WebView(
                viewUrl = "https://www.flightaware.com/live/flight/$fawareId",
                viewModel = viewModel
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "Seleccione un vuelo para ver detalles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
