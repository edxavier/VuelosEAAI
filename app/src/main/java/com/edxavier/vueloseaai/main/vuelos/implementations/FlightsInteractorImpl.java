package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsInteractorImpl implements FlightsInteractor{
    private FlightsRepository repository;

    public FlightsInteractorImpl() {
    }

    @Override
    public List<Vuelos_tbl> execute(int direction) {
        return repository.getFlightsData(direction);
    }
}
