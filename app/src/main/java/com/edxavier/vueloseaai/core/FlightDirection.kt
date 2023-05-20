package com.edxavier.vueloseaai.core

sealed class FlightDirection(var title: String) {
    object Arrival: FlightDirection("Llegadas")
    object Departure: FlightDirection("Salidas")
}