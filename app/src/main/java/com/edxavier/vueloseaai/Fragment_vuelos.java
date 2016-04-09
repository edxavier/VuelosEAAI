package com.edxavier.vueloseaai;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.vueloseaai.asyncTasks.AsynLLegadasInternacionales;
import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelo;
import com.edxavier.vueloseaai.database.model.adapter.AdapterVuelos;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;


public class Fragment_vuelos extends Fragment implements TabLayout.OnTabSelectedListener, TaskResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AsynLLegadasInternacionales internacionales = new AsynLLegadasInternacionales();
    private AdapterVuelos adapterVuelos;
    MaterialDialog pgd;
    private RecyclerView mRecyclerView;
    TabLayout tabLayout;
    TextView empty;
    Tracker tracker;
    long startTime;

    private int tipo_vuelo = Vuelo.INTERNACIONAL;

   // private OnFragmentInteractionListener mListener;

    public Fragment_vuelos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_vuelos, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
        tabLayout.setVisibility(View.VISIBLE);
        if(tabLayout.getTabCount()<=0) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.llegadass)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.salidas)));
        }
        tabLayout.setOnTabSelectedListener(this);
        empty = (TextView) getActivity().findViewById(R.id.empty_message);

        tracker = ((Application) getActivity().getApplication()).getTracker();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
        tracker.enableAdvertisingIdCollection(true);
        tracker.setScreenName("Pantalla Vuelos");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());


        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("ACTION")
                        .setAction("Click ads")
                        .setLabel("Click en publicidad")
                        .build());
            }
        });


        new Delete().from(Vuelo.class).execute();
        internacionales.delegate = this;
        pgd = new MaterialDialog.Builder(getContext())
                .title(R.string.espere)
                .content(getResources().getString(R.string.cargando))
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();
        startTime = System.currentTimeMillis();
        internacionales.execute(getContext());

        //AsynLLegadasInternacionales2 internacionales2 = new AsynLLegadasInternacionales2();
        //internacionales2.delegate = this;
        //internacionales2.execute();

        //AsynSalidasInternacionales asynSalidasInternacionales = new AsynSalidasInternacionales();
        //asynSalidasInternacionales.execute();
        //new AsynLLegadasNacionales().execute();
        //new AsynSalidasNacionales().execute();

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id._recycler_vuelos);
        //emptyMessage = (TextViewHelper) getActivity().findViewById(R.id.empty_blaclist_message);
        //,LinearLayoutManager.VERTICAL, true
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        List<Vuelo> result = null;
        switch (tab.getPosition()){
            case 0:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("Select TAB")
                        .setLabel("LLegadas")
                        .build());
                result = (new Select().from(Vuelo.class)
                        .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                        .and("flujo_vuelo = ? ", String.valueOf(Vuelo.LLEGADA)).execute());
                adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelo>) result);
                mRecyclerView.setAdapter(adapterVuelos);
                if(adapterVuelos.getItemCount()>0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
                break;
            case 1:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("Select TAB")
                        .setLabel("Salidas")
                        .build());
                 result = (new Select().from(Vuelo.class)
                        .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                        .and("flujo_vuelo = ? ", String.valueOf(Vuelo.SALIDA)).execute());
                adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelo>) result);
                mRecyclerView.setAdapter(adapterVuelos);
                if(adapterVuelos.getItemCount()>0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
                break;

        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void loadFinish(ArrayList<Vuelo> vuelos) {
        adapterVuelos = new AdapterVuelos(R.layout.row_vuelos,vuelos);
        mRecyclerView.setAdapter(adapterVuelos);
        if(adapterVuelos.getItemCount()>0)
            empty.setVisibility(View.GONE);
        else
            empty.setVisibility(View.VISIBLE);
        long loadTime = (System.currentTimeMillis() - startTime)/1000;

        tracker.send(new HitBuilders.TimingBuilder()
                .setCategory("Tiempo Carga")
                .setValue(loadTime)
                .setVariable("Carga de datos de vuelo")
                .setLabel("Tiempo Carga de datos de vuelo")
                .build());
        //Log.d("EDER", String.valueOf(loadTime));
        pgd.dismiss();
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    public int getTipo_vuelo() {
        return tipo_vuelo;
    }

    public void setTipo_vuelo(int tipo_vuelo) {
        this.tipo_vuelo = tipo_vuelo;

        try{
            if(tabLayout!=null ) {
                if (tabLayout.getTabCount() > 0 && !tabLayout.getTabAt(0).isSelected()) {
                    tabLayout.getTabAt(0).select();
                } else {
                    List<Vuelo> result = (new Select().from(Vuelo.class)
                            .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                            .and("flujo_vuelo = ? ", String.valueOf(Vuelo.LLEGADA)).execute());
                    adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelo>) result);
                    mRecyclerView.setAdapter(adapterVuelos);
                    if (adapterVuelos.getItemCount() > 0)
                        empty.setVisibility(View.GONE);
                    else
                        empty.setVisibility(View.VISIBLE);
                }
            }

        }catch (Exception e){
            Log.d("EDER", e.getMessage());
        }
    }


    public void refresh(){
        new Delete().from(Vuelo.class).execute();
        pgd = new MaterialDialog.Builder(getContext())
                .title(R.string.espere)
                .content(getResources().getString(R.string.cargando))
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();
        internacionales = new AsynLLegadasInternacionales();
        internacionales.delegate = this;
        startTime = System.currentTimeMillis();
        internacionales.execute(getContext());
    }
}
