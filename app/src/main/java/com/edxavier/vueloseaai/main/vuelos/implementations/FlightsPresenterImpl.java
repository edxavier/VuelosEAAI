package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsPresenterImpl implements FlightsPresenter {
    private EventBusIface eventBus;
    private FlightsView view;
    private FlightsInteractor interactor;

    public FlightsPresenterImpl(EventBusIface eventBus, FlightsView view, FlightsInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        eventBus.register(this);
    }

    @Override
    public void onPause() {
            eventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void getFlightsData(int direction) {
        if(view!=null){
            view.showProgress(true);
        }
        interactor.execute(direction);
    }

    @Override
    @Subscribe
    public void onEventMainThread(FlightsEvents event) {
        String errMsg = event.getError();
        if(view!=null && event.getEvent_type()==FlightsEvents.SQLITE_EVENT){
            //Log.e("EDER", "SQLITE_EVENT: "+ String.valueOf(event.getFlights().size()));
            if(event.getFlights().size()>0){
                view.showElements(false);
            }else{
                view.showElements(true);
            }

            if(errMsg!=null){
                view.onError(errMsg);
            }else {
                if(event.getDirection()==view.getFlightDirection()) {
                    view.setContent(event.getFlights());
                }
            }
        }
    }
}
