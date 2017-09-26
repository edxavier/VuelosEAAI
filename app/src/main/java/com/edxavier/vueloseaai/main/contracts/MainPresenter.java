package com.edxavier.vueloseaai.main.contracts;

import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public interface MainPresenter {
    void onResume();
    void onCreate();
    void onPause();
    void onDestroy();
    void getFlightsData();
}
