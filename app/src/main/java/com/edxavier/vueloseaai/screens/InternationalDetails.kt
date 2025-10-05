package com.edxavier.vueloseaai.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.core.ui.LoadingIndicator
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.google.android.gms.ads.AdSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InternationalDetails(
    viewModel: FlightsViewModel
) {
    val state by viewModel.uiState.collectAsState()
    Column {
        WebView("https://www.flightaware.com/live/flight/AVA396#google_vignette", viewModel)
    }
}