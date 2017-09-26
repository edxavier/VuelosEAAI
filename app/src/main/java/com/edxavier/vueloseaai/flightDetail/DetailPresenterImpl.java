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
    private DetailView view;
    private DetailInteractor interactor;

    public DetailPresenterImpl( DetailView view) {
        this.view = view;
        this.interactor = new DetailInteractorImpl();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause(){
    }

    @Override
    public void onDestroy() {
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
