package com.edxavier.vueloseaai.main.vuelos.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.flightDetail.FlightDetailActivity;
import com.edxavier.vueloseaai.lib.MySnackbar;
import com.edxavier.vueloseaai.main.FlightsActivity;
import com.edxavier.vueloseaai.main.vuelos.adapter.FlightsAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.contracts.FlightsPresenter;
import com.edxavier.vueloseaai.main.vuelos.implementations.FlightsPresenterImpl;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightsFragment extends Fragment implements FlightsView, OnItemClickListener {


    FirebaseAnalytics analytics;

    FlightsPresenter presenter;

    FlightsAdapter adapter;

    Bundle analitycParams;
    @BindView(R.id.lastUpdate)
    TextView lastUpdate;
    @BindView(R.id._recycler_vuelos)
    RecyclerView RecyclerVuelos;
    //@BindView(R.id.empty_message)
    //TextView emptyMessage;
    @BindView(R.id.layout_loading)
    LinearLayout layoutMsg;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    @BindView(R.id.layout_swipe)
    SwipeRefreshLayout layoutSwipe;


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
        Bundle bundle = getArguments();
        this.direction = bundle.getInt("direction");
        presenter = new FlightsPresenterImpl(this, getActivity());
        presenter.onCreate();
        List<Vuelos_tbl> vuelosTbls = new ArrayList<>();
        adapter = new FlightsAdapter(vuelosTbls, this);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(this.direction);
        layoutSwipe.setOnRefreshListener(() -> {
            loadData(this.direction);
            //FlightsActivity activity = (FlightsActivity) getActivity();
            //if(!activity.bp.isPurchased(FlightsActivity.PRODUCT))
                //activity.requestAds();
        });
        layoutSwipe.setColorSchemeResources(R.color.md_deep_orange_500, R.color.md_green_500,
                R.color.md_blue_500 , R.color.md_pink_500);
    }

    private void setupRecyclerView() {
        RecyclerVuelos.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerVuelos.setAdapter(adapter);
        //boolean visible = ((FlightsActivity) getActivity()).isAdVisible();
        //Log.e("EDER_adVisible", String.valueOf(visible));
    }

    public void setRecyclerPadding(int padding){
        if(RecyclerVuelos!=null) {
            try {
                RecyclerVuelos.setPadding(0, 0, 0, padding);
            }catch (Exception ignored){}
        }
    }

    @Override
    public int getFlightDirection() {
        return this.direction;
    }

    @Override
    public void loadData(int direction) {
        //presenter.getFlightsData(direction);
        if (direction == Vuelos_tbl.LLEGADA) {
            presenter.getArrivalsFlightsFromWeb();
        } else if (direction == Vuelos_tbl.SALIDA) {
            presenter.getDeparturesFlightsFromWeb();
        }

    }

    @Override
    public void showEmptyMsg(boolean show) {
        if (show) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            layoutMsg.setVisibility(View.VISIBLE);
        } else {
            layoutSwipe.setRefreshing(false);
            layoutMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(String error) {
        //Snackbar.make(fragmentContainer, error, Snackbar.LENGTH_LONG).show();
        if (direction == Vuelos_tbl.LLEGADA)
            MySnackbar.alert(fragmentContainer, getString(R.string.noarrivalsdata), Snackbar.LENGTH_LONG).show();
        else
            MySnackbar.alert(fragmentContainer, getString(R.string.nodeparturedata), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void setContent(List<Vuelos_tbl> items) {
        adapter.clearFlightsItems();
        adapter.setFlightsItems(items);
        try {
            Vuelos_tbl v = items.get(0);
            lastUpdate.setText(getContext().getResources().getString(R.string.act_msg) + " " + v.getLast_update());
            setupRecyclerView();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(Vuelos_tbl vuelo) {
        FlightsActivity activity = (FlightsActivity) getActivity();
        if(!activity.bp.isPurchased(FlightsActivity.PRODUCT))
            activity.requestAds();
        Intent intent = new Intent(getActivity(), FlightDetailActivity.class);
        intent.putExtra("vuelo", vuelo.getVuelo());
        intent.putExtra("aerolinea", vuelo.getLinea());
        intent.putExtra("isPurchased", activity.bp.isPurchased(FlightsActivity.PRODUCT));
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

    public void startSecuence() {
        if (!Prefs.getBoolean("secuence", false)) {
            FlightsActivity activity = (FlightsActivity) getActivity();

            final Display display = activity.getWindowManager().getDefaultDisplay();
            final Drawable droid = ContextCompat.getDrawable(activity, R.drawable.ic_tap);
            final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
            // Using deprecated methods makes you look way cool
            droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);
            final int[] sec = {0};
            new TapTargetSequence(getActivity())
                    .targets(
                            TapTarget.forToolbarMenuItem(activity.toolbar, R.id.action_rate, getString(R.string.rate), getString(R.string.rate_sec))
                                    .dimColor(android.R.color.black)
                                    .outerCircleColor(R.color.md_green_500)
                                    .targetCircleColor(android.R.color.black)
                                    .transparentTarget(true)
                                    .textColor(android.R.color.white).cancelable(false),
                            TapTarget.forToolbarMenuItem(activity.toolbar, R.id.action_share, getString(R.string.share), getString(R.string.share_sec))
                                    .dimColor(android.R.color.black)
                                    .outerCircleColor(R.color.md_blue_500)
                                    .targetCircleColor(android.R.color.black)
                                    .transparentTarget(true)
                                    .textColor(android.R.color.white).cancelable(false),
                            TapTarget.forBounds(droidTarget, getString(R.string.update), getString(R.string.update_desc))
                                    .cancelable(false)
                                    .icon(droid)
                    ).start();
            Prefs.putBoolean("secuence", true);
        }
    }
}
