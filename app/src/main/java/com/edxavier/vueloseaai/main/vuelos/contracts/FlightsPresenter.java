package com.edxavier.vueloseaai.main.vuelos.contracts;

import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public interface FlightsPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    void getFlightsData(int _direction);
    void onEventMainThread(FlightsEvents event);
}
