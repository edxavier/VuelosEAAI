package com.edxavier.vueloseaai.main.vuelos.events;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsEvents {
    private String error;
    private List<Vuelos_tbl> flights;
    private int event_type;
    private int direction;
    public static int WEB_SCRAP_EVENT = 0;
    public static int SQLITE_EVENT = 1;


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Vuelos_tbl> getFlights() {
        return flights;
    }

    public void setFlights(List<Vuelos_tbl> flights) {
        this.flights = flights;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }
}
