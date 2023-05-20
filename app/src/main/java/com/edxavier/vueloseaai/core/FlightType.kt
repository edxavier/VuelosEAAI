package com.edxavier.vueloseaai.core

sealed class FlightType {
    object International: FlightType()
    object National: FlightType()
}