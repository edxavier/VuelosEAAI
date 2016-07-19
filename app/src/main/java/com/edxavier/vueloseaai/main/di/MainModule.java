package com.edxavier.vueloseaai.main.di;

import android.app.Activity;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.edxavier.vueloseaai.main.contracts.MainInteractor;
import com.edxavier.vueloseaai.main.contracts.MainPresenter;
import com.edxavier.vueloseaai.main.contracts.MainRepository;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.implementations.MainInteractorImpl;
import com.edxavier.vueloseaai.main.implementations.MainPresenterImpl;
import com.edxavier.vueloseaai.main.implementations.MainRepositoryImpl;
import com.edxavier.vueloseaai.main.vuelos.adapter.FlightsAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsInteractorImpl;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsPresenterImpl;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsRepositoryImpl;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Module
public class MainModule {
    private MainView view;
    private Activity activity;
    private OnItemClickListener clickListener;

    public MainModule(Activity activity, MainView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
        this.activity = activity;
    }

    @Provides @Singleton
    MainView providesMainView(){
        return this.view;
    }

    @Provides @Singleton
    MainPresenter provideMainPresenter(EventBusIface eventBus, MainView view, MainInteractor interactor){
        return new MainPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    EventBusIface provideEventBusIface(){
        return new GrobotEventbus();
    }

    @Provides @Singleton
    MainInteractor provideFlightsInteractor(MainRepository repository){
        return new MainInteractorImpl(repository);
    }

    @Provides @Singleton
    MainRepository provideFlightsRepository(EventBusIface eventBus){
        return new MainRepositoryImpl(eventBus, this.activity);
    }

}
