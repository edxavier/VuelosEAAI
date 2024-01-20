package com.edxavier.vueloseaai.data

const val BASE_URL = "https://www.eaai.com.ni"
sealed class FlightsEndpoint(var endpointUrl:String){
    object NatArrivals:FlightsEndpoint("$BASE_URL/pvnac/vuelos_dias_pvnac.php?option=A")
    object NatDepartures:FlightsEndpoint("$BASE_URL/pvnac/vuelos_dias_pvnac.php?option=D")
    object IntArrivals:FlightsEndpoint("$BASE_URL/fids/read_vuelos_dias_fids.php?option=A")
    object IntDepartures:FlightsEndpoint("$BASE_URL/fids/read_vuelos_dias_fids.php?option=D")
}