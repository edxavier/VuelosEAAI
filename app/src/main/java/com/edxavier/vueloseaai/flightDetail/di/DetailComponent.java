package com.edxavier.vueloseaai.flightDetail.di;

import com.edxavier.vueloseaai.flightDetail.FlightDetailActivity;
import com.edxavier.vueloseaai.lib.di.LibsMainModule;
import com.edxavier.vueloseaai.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
@Singleton @Component (modules = { DetailModule.class, LibsMainModule.class})
public interface DetailComponent {
    void inject(FlightDetailActivity activity);
}
