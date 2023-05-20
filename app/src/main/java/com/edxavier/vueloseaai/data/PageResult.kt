package com.edxavier.vueloseaai.data

sealed class PageResult {
    data class Success(val lastUpdate: String, val flights:List<FlightData>):PageResult()
    data class Timeout(val message: String):PageResult()
    data class Error(val message: String):PageResult()
}