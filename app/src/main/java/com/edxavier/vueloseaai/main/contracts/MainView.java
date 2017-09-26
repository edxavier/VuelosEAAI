package com.edxavier.vueloseaai.main.contracts;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public interface MainView {

    void loadData();
    void showElements(boolean show);
    void showProgress(boolean show);
    void onError(String error);
    void setContent(List<Vuelos_tbl> items);
}
