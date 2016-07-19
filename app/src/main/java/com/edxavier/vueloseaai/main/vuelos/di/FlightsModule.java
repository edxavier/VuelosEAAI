package com.edxavier.vueloseaai.main.vuelos.di;

import android.support.v4.app.Fragment;

import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.main.vuelos.adapter.FlightsAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsInteractor;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsRepository;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsInteractorImpl;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsPresenterImpl;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsRepositoryImpl;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Module
public class FlightsModule {
    private FlightsView view;
    private OnItemClickListener clickListener;

    public FlightsModule(FlightsView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

        @Provides @Singleton
        FlightsView providesFlightsView(){
            return this.view;
        }

    @Provides @Singleton
    FlightsPresenter provideFlightsPresenter(EventBusIface eventBus, FlightsView view, FlightsInteractor interactor){
        return new FlightsPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    EventBusIface provideEventBusIface(){
        return new GrobotEventbus();
    }

    @Provides @Singleton
    FlightsInteractor provideFlightsInteractor(FlightsRepository repository){
        return new FlightsInteractorImpl(repository);
    }

    @Provides @Singleton
    FlightsRepository provideFlightsRepository(EventBusIface eventBus){
        return new FlightsRepositoryImpl(eventBus);
    }

    @Provides @Singleton
    FlightsAdapter provideFlightsAdapter(List<Vuelos_tbl> dataset){
        return new FlightsAdapter(dataset, this.clickListener);
    }

    @Provides
    @Singleton
    List<Vuelos_tbl> provideDataset(){
        return new ArrayList<Vuelos_tbl>();
    }

}
