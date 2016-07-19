package com.edxavier.vueloseaai.main.di;

import android.app.Activity;

import com.edxavier.vueloseaai.lib.di.LibsMainModule;
import com.edxavier.vueloseaai.lib.di.LibsModule;
import com.edxavier.vueloseaai.main.FlightsActivity;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Singleton @Component(modules = { MainModule.class, LibsMainModule.class})
public interface MainComponent {
    void inject(FlightsActivity activity);
    //MainPresenter getPresenter();
    //FlightsAdapter getFlightsAdapter();
}
