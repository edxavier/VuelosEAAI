package com.edxavier.vueloseaai.data.repo

import android.util.Log
import com.edxavier.vueloseaai.core.FlightType
import com.edxavier.vueloseaai.data.FlightData
import com.edxavier.vueloseaai.data.PageResult
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage
import org.jsoup.Connection
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

    fun getFlights(endpoint: String, flightType: FlightType): PageResult {
        val flights = mutableListOf<FlightData>()
        Log.w("EDER","getFlights")
        try {
            val webClient = WebClient()
            val myPage: HtmlPage = webClient.getPage(endpoint)
            webClient.waitForBackgroundJavaScript(5000)

            // val doc = Jsoup.connect(endpoint).get()
            val doc = Jsoup.parse(myPage.asXml())
            val div = doc.getElementsByTag("div").first()
            val lastUpdate = getTitle(div)
            val divVuelos = doc.getElementById("vuelos")
            var flightRows = arrayListOf<Element>()
            divVuelos?.let {
                flightRows = divVuelos.getElementsByTag("tr")
            }
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
            if (flights.isEmpty()) {
                return PageResult.Error("No hay resultados para mostrar")
            }
            return  PageResult.Success(flights)
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