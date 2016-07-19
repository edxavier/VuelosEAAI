package com.edxavier.vueloseaai.main.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.contracts.MainInteractor;
import com.edxavier.vueloseaai.main.contracts.MainPresenter;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class MainPresenterImpl implements MainPresenter {
    private EventBusIface eventBus;
    private MainView view;
    private MainInteractor interactor;

    public MainPresenterImpl(EventBusIface eventBus, MainView view, MainInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        eventBus.register(this);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onPause() {
            eventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void getFlightsData() {
        if(view!=null){
            view.showProgress(true);
        }
        interactor.execute();
    }

    @Override
    @Subscribe
    public void onEventMainThread(FlightsEvents event) {
        String errMsg = event.getError();
        if(view!=null && event.getEvent_type()==FlightsEvents.WEB_SCRAP_EVENT){
            view.showProgress(false);
            view.showElements(true);
            if(errMsg!=null){
                view.onError(errMsg);
            }else {
                view.setContent(event.getFlights());
            }
        }
    }
}
