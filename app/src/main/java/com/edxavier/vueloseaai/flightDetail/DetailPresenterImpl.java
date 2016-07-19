package com.edxavier.vueloseaai.flightDetail;

import com.edxavier.vueloseaai.flightDetail.contracts.DetailInteractor;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailPresenter;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailView;
import com.edxavier.vueloseaai.flightDetail.event.DetailEvent;
import com.edxavier.vueloseaai.lib.EventBusIface;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public class DetailPresenterImpl implements DetailPresenter {
    private EventBusIface eventBus;
    private DetailView view;
    private DetailInteractor interactor;

    public DetailPresenterImpl(EventBusIface eventBus, DetailView view, DetailInteractor interactor) {
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
    public void getFlightDetail(String flightNumber) {
        if(view!=null){
            view.showProgress(true);
        }
        interactor.queryFlightDetail(flightNumber);
    }

    @Override
    public void onEventMainThread(DetailEvent event) {

    }
}
