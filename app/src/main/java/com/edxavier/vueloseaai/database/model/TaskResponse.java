package com.edxavier.vueloseaai.database.model;

import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/10/2015.
 */
public interface TaskResponse {
    void loadFinish(FlightsEvents event);

}
