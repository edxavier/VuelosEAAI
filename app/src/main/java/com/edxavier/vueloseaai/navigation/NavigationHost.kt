package com.edxavier.vueloseaai.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.screens.Information
import com.edxavier.vueloseaai.screens.InternationalDetails
import com.edxavier.vueloseaai.screens.Internationals
import com.edxavier.vueloseaai.screens.Nationals
import com.edxavier.vueloseaai.screens.Parking

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
        composable(Destinations.Internationals.route) {
            Internationals(viewModel, navController)
        }
        composable(Destinations.Nationals.route) {
            Nationals(viewModel)
        }
        composable(Destinations.Information.route) {
            Information()
        }
        composable(Destinations.Parking.route) {
            Parking()
        }
        composable(
            route = Destinations.FlightDetails.route,
            arguments = listOf(
                navArgument("flightId") {
                    type = NavType.StringType
                    defaultValue = viewModel.flightId
                }
            )
        ) { backStackEntry ->
            val flightId = backStackEntry.arguments?.getString("flightId") ?: viewModel.flightId
            viewModel.flightId = flightId
            InternationalDetails(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
