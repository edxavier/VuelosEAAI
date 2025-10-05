package com.edxavier.vueloseaai.navigation

sealed class Destinations (
    val route: String,
){
    object Nationals: Destinations("nationals")
    object Internationals: Destinations("internationals")
    object Information: Destinations("information")
    object Parking: Destinations("parking")
    object FlightDetails: Destinations("details")
}