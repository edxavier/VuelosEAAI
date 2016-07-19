package com.edxavier.vueloseaai.flightDetail.contracts;

import com.edxavier.vueloseaai.flightDetail.event.DetailEvent;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public interface DetailPresenter {
    void onResume();
    void onCreate();
    void onPause();
    void onDestroy();
    void getFlightDetail(String flightNumber);
    void onEventMainThread(DetailEvent event);
}
