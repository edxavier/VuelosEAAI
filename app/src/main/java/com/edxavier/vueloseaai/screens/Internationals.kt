package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.data.FlightsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Internationals(
    viewModel: FlightsViewModel,
    navCtrl: NavHostController
) {
    val scrapeVuelosInt by viewModel.scrapeVuelosInt.collectAsState()
    val eaaiIntUrl by viewModel.eaaiIntUrl.collectAsState()

    if (scrapeVuelosInt) {
        Flights(flightType = FlightType.International, viewModel = viewModel, navCtrl = navCtrl)
    } else {
        WebView(eaaiIntUrl, viewModel)
    }
}