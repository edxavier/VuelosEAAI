package com.edxavier.vueloseaai.flightDetail;

import android.app.Activity;
import android.util.Log;

import com.edxavier.vueloseaai.flightDetail.contracts.DetailRepository;
import com.edxavier.vueloseaai.lib.EventBusIface;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public class DetailRepositoryimpl implements DetailRepository {
    private EventBusIface eventBus;
    private Activity view;

    public DetailRepositoryimpl(EventBusIface eventBus, Activity view) {
        this.eventBus = eventBus;
        this.view = view;
    }

    @Override
    public void queryAviancaFlight(String flightNumber) {
        Log.e("EDER", "queryAviancaFlight");
    }
}
