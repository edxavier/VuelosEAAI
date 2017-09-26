package com.edxavier.vueloseaai.flightDetail;

import com.edxavier.vueloseaai.flightDetail.contracts.DetailInteractor;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailRepository;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public class DetailInteractorImpl implements DetailInteractor {
    DetailRepository repository;

    public DetailInteractorImpl() {
        this.repository = new DetailRepositoryimpl();
    }

    @Override
    public void queryFlightDetail(String flightNumber) {
        repository.queryAviancaFlight(flightNumber);
    }
}
