package com.edxavier.vueloseaai;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.ads.MobileAds;
import com.pixplicity.easyprefs.library.Prefs;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());

        FlowManager.init(new FlowConfig.Builder(this).build());
        MobileAds.initialize(this, "ca-app-pub-9964109306515647~8960716619");
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }


/*
    public synchronized  Tracker getTracker() {
        return tracker;
    }
    */
}
