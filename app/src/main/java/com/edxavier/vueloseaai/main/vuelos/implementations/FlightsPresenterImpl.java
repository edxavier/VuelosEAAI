package com.edxavier.vueloseaai.main.vuelos.implementations;

import android.content.Context;
import android.util.Log;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.lib.RxBus;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsPresenterImpl implements FlightsPresenter {
    private FlightsView view;
    private FlightsRepository repository;
    private RxBus rxBus;
    private Disposable subs;
    private Context ctx;


    public FlightsPresenterImpl( FlightsView view, Context context) {
        this.view = view;
        this.ctx = context;
        this.repository = new FlightsRepositoryImpl(this.ctx);
        this.rxBus = RxBus.getInstance();
    }

    @Override
    public void onResume() {
        if(subs.isDisposed())
            setupSubscription();
    }

    private void setupSubscription(){

        subs = rxBus.register(FlightsEvents.class, events -> {
            if(events.getEventType() == FlightsEvents.DATA &&
                    events.getEventDirection() == view.getFlightDirection()){
                getFlightsData(view.getFlightDirection());
            }else  if(events.getEventType() == FlightsEvents.ERROR &&
                    events.getEventDirection() == view.getFlightDirection()){
                getFlightsData(view.getFlightDirection());
                view.onError("");
            }
        });
    }

    @Override
    public void onPause() {
        subs.dispose();
    }

    @Override
    public void onDestroy() {
        view = null;
        rxBus = null;
        subs.dispose();
    }

    @Override
    public void onCreate() {
        setupSubscription();
    }

    @Override
    public void getFlightsData(int direction) {
        if(direction == Vuelos_tbl.LLEGADA)
            view.startSecuence();
        List<Vuelos_tbl> res = repository.getFlightsData(direction);
        view.showProgress(false);
        if(!res.isEmpty()) {
            view.setContent(res);
            view.showEmptyMsg(false);
        }else
            view.showEmptyMsg(true);
    }

    @Override
    public void getArrivalsFlightsFromWeb() {
        repository.getArrivalsFlightsFromWeb();
    }

    @Override
    public void getDeparturesFlightsFromWeb() {
        repository.getDeparturesFlightsFromWeb();
    }

}
