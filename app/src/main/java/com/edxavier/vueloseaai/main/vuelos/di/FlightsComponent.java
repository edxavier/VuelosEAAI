package com.edxavier.vueloseaai.main.vuelos.di;

import com.edxavier.vueloseaai.lib.di.LibsModule;
import com.edxavier.vueloseaai.main.vuelos.adapter.FlightsAdapter;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Singleton @Component(modules = { FlightsModule.class, LibsModule.class})
public interface FlightsComponent {
    void inject(FlightsFragment fragment);
    //MainPresenter getPresenter();
    //FlightsAdapter getFlightsAdapter();
}
