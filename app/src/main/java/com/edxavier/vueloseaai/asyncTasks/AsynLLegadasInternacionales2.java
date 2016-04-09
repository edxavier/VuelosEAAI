package com.edxavier.vueloseaai.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsynLLegadasInternacionales2 extends AsyncTask<Void, Void, ArrayList<Vuelo>> {

    public TaskResponse delegate = null;

    @Override
    protected ArrayList<Vuelo> doInBackground(Void... params) {
        ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
        //delegate = params[0];
        try{
            Document doc = Jsoup.connect("http://www.eaai.com.ni/vuelos/solovuelos.php").get();
            Elements rows = doc.getElementsByClass("listado");
            for (Element link : rows) {
                Vuelo vuelo = new Vuelo();
                vuelo.setLogo(link.select("img").get(0).attr("src"));
                vuelo.setLinea(link.select("font").get(1).text().replaceAll("^\\s+", ""));
                vuelo.setEstatus(link.select("font").get(6).text());
                vuelo.setVuelo(link.select("font").get(3).text());
                vuelo.setCiudad(link.select("font").get(4).text());
                vuelo.setHora(link.select("font").get(5).text());
                vuelo.setTipo_vuelo(Vuelo.INTERNACIONAL);
                vuelo.setFlujo_vuelo(Vuelo.LLEGADA);
                vuelos.add(vuelo);
                vuelo.save();
            }

            doc = Jsoup.connect("http://www.eaai.com.ni/vuelos/solovuelos.php?tipo=S").get();
            rows = doc.getElementsByClass("listado");
            for (Element link : rows) {
                Vuelo vuelo = new Vuelo();
                vuelo.setLogo(link.select("img").get(0).attr("src"));
                vuelo.setLinea(link.select("font").get(1).text().replaceAll("^\\s+", ""));
                vuelo.setEstatus(link.select("font").get(6).text());
                vuelo.setVuelo(link.select("font").get(3).text());
                vuelo.setCiudad(link.select("font").get(4).text());
                vuelo.setHora(link.select("font").get(5).text());
                vuelo.setTipo_vuelo(Vuelo.INTERNACIONAL);
                vuelo.setFlujo_vuelo(Vuelo.SALIDA);
                //vuelos.add(vuelo);
                vuelo.save();

            }



        }
        catch(Exception e){         //if an HTTP/connection error occurs, handle JauntException.
            return vuelos;
        }
        return vuelos;
    }

    @Override
    protected void onPostExecute(ArrayList<Vuelo> vuelos) {
        super.onPostExecute(vuelos);
        if(delegate!=null)
        {
            delegate.loadFinish(vuelos);
        }
        else
        {
            Log.e("EDER", "You have not assigned IApiAccessResponse delegate");
        }
    }


}
