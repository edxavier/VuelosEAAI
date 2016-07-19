package com.edxavier.vueloseaai.main.implementations;

import android.app.Activity;
import android.util.Log;

import com.edxavier.vueloseaai.asyncTasks.AsynLLegadasInternacionales;
import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.contracts.MainRepository;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class MainRepositoryImpl implements MainRepository, TaskResponse {
    private EventBusIface eventBus;
    Activity view;
    private AsynLLegadasInternacionales internacionales;

    public MainRepositoryImpl(EventBusIface eventBus,  Activity view) {
        this.eventBus = eventBus;
        this.view = view;
    }

    @Override
    public void loadFinish(FlightsEvents event) {
       eventBus.post(event);
    }

    @Override
    public void getFlightsDataFromWeb() {
        internacionales = new AsynLLegadasInternacionales();
        internacionales.delegate = this;
        internacionales.execute(this.view);
    }
}
