package com.edxavier.vueloseaai.main.vuelos.events;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsEvents {
    private String error;
    private int eventType;
    private int eventDirection;
    public static int ERROR = 0;
    public static int DATA = 1;

    public FlightsEvents( int eventType, int eventDirection, String error) {
        this.error = error;
        this.eventType = eventType;
        this.eventDirection = eventDirection;
    }

    public FlightsEvents() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventDirection() {
        return eventDirection;
    }

    public void setEventDirection(int eventDirection) {
        this.eventDirection = eventDirection;
    }
}
