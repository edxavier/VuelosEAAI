package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.google.android.gms.ads.AdSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Internationals(
    viewModel: FlightsViewModel
) {
    Flights(flightType = FlightType.International, viewModel = viewModel)
}