package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsInteractorImpl implements FlightsInteractor{
    private FlightsRepository repository;

    public FlightsInteractorImpl(FlightsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(int direction) {
        repository.getFlightsData(direction);
    }
}
