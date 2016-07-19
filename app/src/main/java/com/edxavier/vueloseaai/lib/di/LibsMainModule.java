package com.edxavier.vueloseaai.lib.di;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Module
public class LibsMainModule {
    Activity activity;
    //****** CONSTRUCTORES *******

    public LibsMainModule(Activity activity) {
        this.activity = activity;
    }
    //****** FIN CONSTRUCTORES *******

    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(Activity activity){
        return FirebaseAnalytics.getInstance(this.activity);
    }

    @Provides
    @Singleton
    Activity providesFragment(){
        return this.activity;
    }

    @Provides
    @Singleton
    GrobotEventbus provideGrobotEventbus(){
        return new GrobotEventbus();
    }

}
