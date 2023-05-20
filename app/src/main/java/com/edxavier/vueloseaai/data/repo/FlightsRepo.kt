package com.edxavier.vueloseaai.data.repo

import com.edxavier.vueloseaai.data.FlightData
import com.edxavier.vueloseaai.data.PageResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class FlightsRepo {

    private fun getTitle(el: Element?): String{
        var title = ""
        el?.let {//Get the last time the data was updated
            title = it.text().split(": ").last()
        }
        return title
    }

    fun getFlights(endpoint: String): PageResult {
        val flights = mutableListOf<FlightData>()
        try {
            val doc = Jsoup.connect(endpoint).get()

            val div = doc.getElementsByTag("div").first()
            val lastUpdate = getTitle(div)

            val flightRows = doc.getElementsByTag("tr")
            if(flightRows.isNotEmpty())
                flightRows.removeFirst()
            flightRows.forEach { data->
                val cols = data.children()
                var gate = ""
                if(cols.size == 6){
                    gate = "Puerta ${cols[5].html()}"
                }
                val imgEl = cols[0].children().first()
                val imgUrl = if(imgEl != null) imgEl.attr("src") else ""
                val flight = FlightData(
                    logo = imgUrl,
                    flight = cols[1].html(),
                    origin = cols[2].html(),
                    time = cols[3].html(),
                    status = cols[4].html(),
                    gate = gate,
                )
                flights.add(flight)
            }
            return PageResult.Success(
                lastUpdate = lastUpdate,
                flights = flights
            )
        }
        catch (e: SocketTimeoutException){
            return PageResult.Timeout("Verifique su conexion de internet y vuelva a intentarlo.")
        }
        catch (e: UnknownHostException){
            return PageResult.Error("No es posible acceder al servidor, verifique su conexion de internet y vuelva a intentarlo.")
        }
        catch (e: Exception){
            return PageResult.Error("Ha ocurrido un error inesperado: ${e.message}")
        }
    }
}