package com.edxavier.vueloseaai.lib.di;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
@Module
public class LibsModule {
    Fragment fragment;
    //****** CONSTRUCTORES *******

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }
    //****** FIN CONSTRUCTORES *******

    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(Fragment fragment){
        return FirebaseAnalytics.getInstance(fragment.getContext());
    }

    @Provides
    @Singleton
    Fragment providesFragment(){
        return this.fragment;
    }

    @Provides
    @Singleton
    GrobotEventbus provideGrobotEventbus(){
        return new GrobotEventbus();
    }

}
