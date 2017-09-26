package com.edxavier.vueloseaai.main.vuelos.contracts;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 17/06/2016.
 */
public interface FlightsRepository {
    List<Vuelos_tbl> getFlightsData(int direction);
    void getArrivalsFlightsFromWeb();
    void getDeparturesFlightsFromWeb();
}
