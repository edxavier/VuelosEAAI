package com.edxavier.vueloseaai.main.vuelos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edxavier.vueloseaai.EaaiApplication;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.flightDetail.FlightDetailActivity;
import com.edxavier.vueloseaai.main.vuelos.adapter.FlightsAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.di.FlightsComponent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FlightsFragment extends Fragment implements FlightsView, OnItemClickListener {

    @Bind(R.id._recycler_vuelos)
    RecyclerView RecyclerVuelos;
    @Bind(R.id.empty_message)
    TextView emptyMessage;
    @Bind(R.id.layout_msg)
    LinearLayout layoutMsg;

    @Inject
    FirebaseAnalytics analytics;
    @Inject
    FlightsPresenter presenter;
    @Inject
    FlightsAdapter adapter;
    @Bind(R.id.fragmentContainer)
    FrameLayout fragmentContainer;

    Bundle analitycParams;
    SweetAlertDialog pgd;
    boolean showProgress = false;
    @Bind(R.id.lastUpdate)
    TextView lastUpdate;
    private int direction;


    public FlightsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flights_fragments, container, false);
        ButterKnife.bind(this, view);
        pgd = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);

        analitycParams = new Bundle();
        setupInjection();
        setupRecyclerView();
        Bundle bundle = getArguments();
        this.direction = bundle.getInt("direction");
        loadData(this.direction);
        return view;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private void setupRecyclerView() {
        RecyclerVuelos.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerVuelos.setAdapter(adapter);
    }

    private void setupInjection() {
        EaaiApplication app = (EaaiApplication) getActivity().getApplication();
        FlightsComponent component = app.getFlightsComponent(this, this, this);
        component.inject(this);
    }

    @Override
    public int getFlightDirection() {
        return this.direction;
    }

    @Override
    public void loadData(int direction) {
        presenter.getFlightsData(direction);
    }

    @Override
    public void showElements(boolean show) {
        if (show) {
            layoutMsg.setVisibility(View.VISIBLE);
        } else {
            layoutMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {


        } else {
        }
    }

    @Override
    public void onError(String error) {
        Snackbar.make(fragmentContainer, error, Snackbar.LENGTH_LONG).show();
        showProgress(false);
        analitycParams.putString("flights_error", error);
        analytics.logEvent("list_flights", analitycParams);
    }

    @Override
    public void setContent(List<Vuelos_tbl> items) {
        adapter.setFlightsItems(items);
        try {
            Vuelos_tbl v = items.get(0);
            lastUpdate.setText(getContext().getResources().getString(R.string.act_msg) + " " + v.getLast_update());
        }catch (Exception e){}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(Vuelos_tbl vuelo) {

        Intent intent = new Intent(getActivity(), FlightDetailActivity.class);
        intent.putExtra("vuelo", vuelo.getVuelo());
        intent.putExtra("aerolinea", vuelo.getLinea());
        startActivity(intent);

    }


    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }
}
