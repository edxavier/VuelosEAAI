package com.edxavier.vueloseaai.flightDetail.di;

import android.app.Activity;

import com.edxavier.vueloseaai.flightDetail.DetailInteractorImpl;
import com.edxavier.vueloseaai.flightDetail.DetailPresenterImpl;
import com.edxavier.vueloseaai.flightDetail.DetailRepositoryimpl;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailInteractor;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailPresenter;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailRepository;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailView;
import com.edxavier.vueloseaai.lib.EventBusIface;
import com.edxavier.vueloseaai.lib.GrobotEventbus;
import com.edxavier.vueloseaai.main.contracts.MainView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
@Module
public class DetailModule {
    private DetailView view;
    private Activity activity;

    public DetailModule(DetailView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    DetailView providesDetailView(){
        return this.view;
    }

    @Provides @Singleton
    DetailPresenter provideDetailPresenter(EventBusIface eventBus, DetailView view, DetailInteractor interactor){
        return new DetailPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    EventBusIface provideEventBusIface(){
        return new GrobotEventbus();
    }

    @Provides @Singleton
    DetailInteractor provideDetailInteractor(DetailRepository repository){
        return new DetailInteractorImpl(repository);
    }

    @Provides @Singleton
    DetailRepository provideDetailRepository(EventBusIface eventBus){
        return new DetailRepositoryimpl(eventBus, this.activity);
    }

}
