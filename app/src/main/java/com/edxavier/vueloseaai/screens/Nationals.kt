package com.edxavier.vueloseaai.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.edxavier.vueloseaai.core.FlightDirection
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.google.android.gms.ads.AdSize

@Composable
fun Nationals(
    viewModel: FlightsViewModel,
    adSize: AdSize
) {
    Flights(flightType = FlightType.National, viewModel = viewModel, adSize = adSize)
}