package com.edxavier.vueloseaai.main.vuelos.ui;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public interface FlightsView {
    void startSecuence();
    int getFlightDirection();
    void loadData(int direction);
    void showEmptyMsg(boolean show);
    void showProgress(boolean show);
    void onError(String error);
    void setContent(List<Vuelos_tbl> items);
}
