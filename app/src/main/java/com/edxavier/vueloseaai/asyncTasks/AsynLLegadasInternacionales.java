
package com.edxavier.vueloseaai.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.TaskResponse;
//import com.edxavier.vueloseaai.database.model.Vuelo;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public class AsynLLegadasInternacionales extends AsyncTask<Context, Void, FlightsEvents> {

    public TaskResponse delegate = null;

    @Override
    protected FlightsEvents doInBackground(Context... params) {
        ArrayList<Vuelos_tbl> vuelos = new ArrayList<Vuelos_tbl>();
        //delegate = params[0];
        FlightsEvents event = new FlightsEvents();
        Context ctx = params[0];

        try{
            Document doc = Jsoup.connect("http://www.eaai.com.ni/fids/read_vuelos_dias_fids.php?option=A").get();

            //ya que solo existe una tabla  en la pagina, seleccionamos directamentes todas las filas
            Elements rows = doc.select("tr");
            Elements div = doc.select("div");
            String [] texto = div.get(0).text().split(" ");
            String ultima_act = texto[texto.length-2]+" "+ texto[texto.length-1];
            //eliminarla fila de encabezado
            rows.remove(0);
            //recorrer las filas y acceder al valor de cada columna
            if(rows.size()>0)
                SQLite.delete().from(Vuelos_tbl.class).execute();

            for (Element row : rows) {
                Vuelos_tbl vuelo = new Vuelos_tbl();
                String img = row.select("td").get(0).select("img").attr("src");
                String p = img.split("\\/")[5];
                if(p.endsWith(".jpg")){
                    String line = vuelo.getAerolinea(p.replaceAll("^\\s+", ""));
                    vuelo.setLinea(line);
                }else
                    vuelo.setLinea("Linea no reconocida");

                vuelo.setEstatus(row.select("td").get(4).text());
                vuelo.setVuelo(row.select("td").get(1).text());
                vuelo.setCiudad(row.select("td").get(2).text());
                vuelo.setHora(row.select("td").get(3).text());
                vuelo.setTipo_vuelo(Vuelos_tbl.INTERNACIONAL);
                vuelo.setFlujo_vuelo(Vuelos_tbl.LLEGADA);
                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                vuelo.setLast_update(dt.format(new Date()));
                vuelo.setMsg(ultima_act);
                vuelos.add(vuelo);
                vuelo.save();


            }

            doc = Jsoup.connect("http://www.eaai.com.ni/fids/read_vuelos_dias_fids.php?option=D").get();
            rows = doc.select("tr");
            rows.remove(0);
            for (Element row : rows) {
                Vuelos_tbl vuelo = new Vuelos_tbl();
                String img = row.select("td").get(0).select("img").attr("src");
                String p = img.split("\\/")[5];
                if(p.endsWith(".jpg")){
                    String line = vuelo.getAerolinea(p.replaceAll("^\\s+", ""));
                    vuelo.setLinea(line);
                }else
                    vuelo.setLinea("Linea no reconocida");

                vuelo.setEstatus(row.select("td").get(4).text());
                vuelo.setVuelo(row.select("td").get(1).text());
                vuelo.setCiudad(row.select("td").get(2).text());
                vuelo.setHora(row.select("td").get(3).text() + "\n"+ ctx.getResources().getString(R.string.puerta) + row.select("td").get(5).text());
                vuelo.setTipo_vuelo(Vuelos_tbl.INTERNACIONAL);
                vuelo.setFlujo_vuelo(Vuelos_tbl.SALIDA);
                vuelo.setMsg(ultima_act);
                //vuelos.add(vuelo);
                vuelo.save();
                //Log.d("EDER", row.select("td").get(1).text());
            }
/*
            doc = Jsoup.connect("http://www.eaai.com.ni/vuelos/solovuelos.php").get();
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
            */



        }
        catch(Exception e){      //if an HTTP/connection error occurs, handle JauntException.
            //Log.d("EDER_EXCEP", e.getMessage());
            event.setError(ctx.getResources().getString(R.string.download_error));

            return event;
        }

        event.setError(null);
        return event;
    }

    @Override
    protected void onPostExecute(FlightsEvents event) {
        super.onPostExecute(event);
        if(delegate!=null)
        {
            delegate.loadFinish(event);
        }
        else
        {
            //Log.e("EDER", "You have not assigned IApiAccessResponse delegate");
        }
    }


}
