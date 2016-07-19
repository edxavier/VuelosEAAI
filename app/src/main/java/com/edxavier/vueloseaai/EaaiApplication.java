package com.edxavier.vueloseaai;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailView;
import com.edxavier.vueloseaai.flightDetail.di.DaggerDetailComponent;
import com.edxavier.vueloseaai.flightDetail.di.DetailComponent;
import com.edxavier.vueloseaai.flightDetail.di.DetailModule;
import com.edxavier.vueloseaai.lib.di.LibsMainModule;
import com.edxavier.vueloseaai.lib.di.LibsModule;
import com.edxavier.vueloseaai.main.contracts.MainView;

import com.edxavier.vueloseaai.main.di.DaggerMainComponent;
import com.edxavier.vueloseaai.main.di.MainComponent;
import com.edxavier.vueloseaai.main.di.MainModule;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.di.DaggerFlightsComponent;
import com.edxavier.vueloseaai.main.vuelos.di.FlightsComponent;
import com.edxavier.vueloseaai.main.vuelos.di.FlightsModule;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsView;
import com.google.android.gms.ads.MobileAds;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Eder Xavier Rojas on 16/11/2015.
 */
public class EaaiApplication extends android.app.Application {
    //public static GoogleAnalytics analytics;
    //public static Tracker tracker;
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-70090724-3";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //analytics = GoogleAnalytics.getInstance(this);
       // analytics.setLocalDispatchPeriod(800);
       // tracker = analytics.newTracker(PROPERTY_ID); // Replace with actual tracker/property Id
        //tracker.enableExceptionReporting(true);
        FlowManager.init(new FlowConfig.Builder(this).build());
        MobileAds.initialize(this, "ca-app-pub-9964109306515647~8960716619");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }

    public FlightsComponent getFlightsComponent(Fragment fragment, FlightsView view, OnItemClickListener listener){
        return DaggerFlightsComponent.builder()
                .libsModule(new LibsModule(fragment))
                .flightsModule(new FlightsModule(view,listener))
                .build();
    }

    public MainComponent getMainComponent(Activity activity, MainView view, OnItemClickListener listener ){
        return DaggerMainComponent.builder()
                .libsMainModule(new LibsMainModule(activity))
                .mainModule(new MainModule(activity, view, listener))
                .build();
    }

    public DetailComponent getDetailComponent(Activity activity, DetailView view){
        return  DaggerDetailComponent.builder()
                .libsMainModule(new LibsMainModule(activity))
                .detailModule(new DetailModule(view, activity))
                .build();/**/
    }

/*
    public synchronized  Tracker getTracker() {
        return tracker;
    }
    */
}
