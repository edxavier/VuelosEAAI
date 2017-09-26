package com.edxavier.vueloseaai.flightDetail.contracts;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.flightDetail.event.DetailEvent;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public interface DetailView {

    void loadData();
    void showElements(boolean show);
    void showProgress(boolean show);
    void onError(String error);
    void setContent(DetailEvent event);
}
