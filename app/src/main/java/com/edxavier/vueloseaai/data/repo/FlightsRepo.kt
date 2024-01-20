package com.edxavier.vueloseaai.data.repo

import android.util.Log
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
            // val webClient = WebClient(BrowserVersion.CHROME)
            // webClient.waitForBackgroundJavaScript(5000)
            // val myPage: HtmlPage = webClient.getPage(endpoint)
            // webClient.waitForBackgroundJavaScript(5000)

            val doc = Jsoup.connect(endpoint).get()
            // val doc = Jsoup.parse(myPage.asXml())
            // val div = doc.getElementsByTag("div").first()
            // val lastUpdate = getTitle(div)
            val divVuelos = doc.getElementsByTag("table")
            var flightRows = arrayListOf<Element>()
            if (divVuelos.size> 0){
                flightRows = divVuelos[0].getElementsByTag("tr")
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
                throw Exception("No hay datos")
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
        //catch (e: Exception){
        //    return PageResult.Error("Ha ocurrido un error inesperado: ${e.message}")
        //}
    }
}