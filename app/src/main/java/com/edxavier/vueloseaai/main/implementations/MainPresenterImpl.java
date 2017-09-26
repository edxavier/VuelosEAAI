package com.edxavier.vueloseaai.main.implementations;

import android.util.Log;

import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.contracts.MainInteractor;
import com.edxavier.vueloseaai.main.contracts.MainPresenter;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView view;
    private MainInteractor interactor;

    public MainPresenterImpl(EventBusIface eventBus, MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume(){
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void getFlightsData() {
        if(view!=null){
            view.showProgress(true);
        }
        interactor.execute();
    }


}
