package com.edxavier.vueloseaai.main.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.main.contracts.MainInteractor;
import com.edxavier.vueloseaai.main.contracts.MainRepository;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class MainInteractorImpl implements MainInteractor {
    private MainRepository repository;

    public MainInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getFlightsDataFromWeb();
    }
}
