package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.content.Context;
import android.util.Log;

import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.asyncTasks.AsynLLegadasInternacionales;
import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl_Table;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.lib.RxBus;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsRepositoryImpl implements FlightsRepository {
    private RxBus rxBus;
    private Context ctx;

    FlightsRepositoryImpl(Context context) {
        this.rxBus = RxBus.getInstance();
        this.ctx = context;
    }

    @Override
    public  List<Vuelos_tbl> getFlightsData(int direction) {
        List<Vuelos_tbl> result = null;
        if(direction==Vuelos_tbl.LLEGADA) {
             result = SQLite.select().from(Vuelos_tbl.class)
                    .where(Vuelos_tbl_Table.tipo_vuelo.eq(Vuelos_tbl.INTERNACIONAL))
                    .and(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.LLEGADA)).queryList();
        }else if(direction==Vuelos_tbl.SALIDA){
            result = SQLite.select().from(Vuelos_tbl.class)
                    .where(Vuelos_tbl_Table.tipo_vuelo.eq(Vuelos_tbl.INTERNACIONAL))
                    .and(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.SALIDA)).queryList();
        }
        return result;
    }

    @Override
    public void getArrivalsFlightsFromWeb() {
        Flowable.fromCallable(() -> {

                Document doc = Jsoup.connect("https://www.eaai.com.ni/fids/read_vuelos_dias_fids.php?option=A").get();

                //ya que solo existe una tabla  en la pagina, seleccionamos directamentes todas las filas
                Elements rows = doc.select("tr");
                Elements div = doc.select("div");
                String[] texto = div.get(0).text().split(" ");
                String ultima_act = texto[texto.length - 2] + " " + texto[texto.length - 1];
                //eliminarla fila de encabezado
                rows.remove(0);
                //recorrer las filas y acceder al valor de cada columna
                if (rows.size() > 0) {
                    SQLite.delete().from(Vuelos_tbl.class).where(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.LLEGADA)).execute();
                }
                for (Element row : rows) {
                    Vuelos_tbl vuelo = new Vuelos_tbl();
                    String img = row.select("td").get(0).select("img").attr("src");
                    String p = img.split("\\/")[5];
                    if (p.endsWith(".jpg")) {
                        String line = vuelo.getAerolinea(p.replaceAll("^\\s+", ""));
                        vuelo.setLinea(line);
                    } else
                        vuelo.setLinea("Linea no reconocida");

                    vuelo.setEstatus(row.select("td").get(4).text());
                    vuelo.setVuelo(row.select("td").get(1).text());
                    vuelo.setCiudad(row.select("td").get(2).text());
                    vuelo.setHora(row.select("td").get(3).text());
                    vuelo.setTipo_vuelo(Vuelos_tbl.INTERNACIONAL);
                    vuelo.setFlujo_vuelo(Vuelos_tbl.LLEGADA);
                    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
                    vuelo.setLast_update(dt.format(new Date()));
                    vuelo.setMsg(ultima_act);
                    vuelo.save();
                }
            return "";
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    FlightsEvents events = new FlightsEvents(FlightsEvents.DATA, Vuelos_tbl.LLEGADA, null);
                    rxBus.post(events);
                }, throwable -> {
                    Log.e("EDER", throwable.getMessage());
                    FlightsEvents events = new FlightsEvents(FlightsEvents.ERROR, Vuelos_tbl.LLEGADA,
                            throwable.getMessage());
                    rxBus.post(events);
                });
    }

        @Override
    public void getDeparturesFlightsFromWeb() {
            Flowable.fromCallable(() -> {

                Document doc = Jsoup.connect("https://www.eaai.com.ni/fids/read_vuelos_dias_fids.php?option=D").get();
                //ya que solo existe una tabla  en la pagina, seleccionamos directamentes todas las filas
                Elements rows = doc.select("tr");
                rows.remove(0);
                if (rows.size() > 0)
                    SQLite.delete().from(Vuelos_tbl.class).where(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.SALIDA)).execute();
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
                    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
                    vuelo.setLast_update(dt.format(new Date()));
                    vuelo.setMsg(dt.format(new Date()));
                    vuelo.save();
                }
                return "";
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        FlightsEvents events = new FlightsEvents(FlightsEvents.DATA, Vuelos_tbl.SALIDA, null);
                        rxBus.post(events);
                    },
                    throwable -> {
                        FlightsEvents events = new FlightsEvents(FlightsEvents.ERROR, Vuelos_tbl.SALIDA,
                            throwable.getMessage());
                        rxBus.post(events);
                    });
    }
}
