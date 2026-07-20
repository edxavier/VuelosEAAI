package com.edxavier.vueloseaai.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.edxavier.vueloseaai.data.FlightsViewModel

@Composable
fun Nationals(
    viewModel: FlightsViewModel,
) {
    val eaaiNacUrl by viewModel.eaaiNacUrl.collectAsState()
    WebView(eaaiNacUrl, viewModel)
}