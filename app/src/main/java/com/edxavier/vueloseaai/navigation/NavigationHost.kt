package com.edxavier.vueloseaai.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.screens.Information
import com.edxavier.vueloseaai.screens.Internationals
import com.edxavier.vueloseaai.screens.Nationals
import com.edxavier.vueloseaai.screens.Parking
import com.edxavier.vueloseaai.screens.WebView
import com.google.android.gms.ads.AdSize

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: FlightsViewModel,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Internationals.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Destinations.Internationals.route){
            Internationals(viewModel)
        }
        composable(Destinations.Nationals.route){
            Nationals(viewModel)
        }
        composable(Destinations.Information.route){
            Information()
        }
        composable(Destinations.Parking.route){
            Parking()
        }
    }
}