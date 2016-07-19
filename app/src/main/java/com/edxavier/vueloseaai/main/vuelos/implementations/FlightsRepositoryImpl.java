package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.asyncTasks.AsynLLegadasInternacionales;
import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl_Table;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsRepositoryImpl implements FlightsRepository {
    private EventBusIface eventBus;
    private AsynLLegadasInternacionales internacionales = new AsynLLegadasInternacionales();

    public FlightsRepositoryImpl(EventBusIface eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getFlightsData(int direction) {
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
        FlightsEvents event = new FlightsEvents();
        event.setEvent_type(FlightsEvents.SQLITE_EVENT);
        event.setDirection(direction);
        event.setFlights(result);
        event.setError(null);
        eventBus.post(event);
    }
}
