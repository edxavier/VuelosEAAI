package com.edxavier.vueloseaai.asyncTasks;

import android.os.AsyncTask;

import com.edxavier.vueloseaai.database.model.Vuelo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsynSalidasNacionales extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
        try{
            Document doc = Jsoup.connect("http://www.eaai.com.ni/vuelos/solovuelosn.php?tipo=S").get();
            Elements rows = doc.getElementsByClass("listado");
            for (Element link : rows) {
                Vuelo vuelo = new Vuelo();
                vuelo.setLogo(link.select("img").get(0).attr("src"));
                vuelo.setLinea(link.select("font").get(1).text().replaceAll("^\\s+", ""));
                vuelo.setEstatus(link.select("font").get(6).text());
                vuelo.setVuelo(link.select("font").get(3).text());
                vuelo.setCiudad(link.select("font").get(4).text());
                vuelo.setHora(link.select("font").get(5).text());
                vuelo.setTipo_vuelo(Vuelo.NACIONAL);
                vuelo.setFlujo_vuelo(Vuelo.SALIDA);
                vuelos.add(vuelo);
                vuelo.save();
            }
        }
        catch(Exception e){         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
            //Log.d("EDER_SAL_NAC",e.getMessage());
        }
        return null;
    }
}
