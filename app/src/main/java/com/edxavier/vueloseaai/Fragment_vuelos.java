package com.edxavier.vueloseaai;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.afollestad.materialdialogs.MaterialDialog;
import com.edxavier.vueloseaai.asyncTasks.AsynLLegadasInternacionales;
import com.edxavier.vueloseaai.database.model.TaskResponse;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
//import com.edxavier.vueloseaai.database.model.Vuelos_tbl_Table;
import com.edxavier.vueloseaai.database.model.adapter.AdapterVuelos;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

import java.util.List;


public class Fragment_vuelos extends Fragment implements TabLayout.OnTabSelectedListener, TaskResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AsynLLegadasInternacionales internacionales = new AsynLLegadasInternacionales();
    private AdapterVuelos adapterVuelos;
    private RecyclerView mRecyclerView;
    TabLayout tabLayout;
    TextView empty;
    TextView msg;
    //Tracker tracker;
    long startTime;

    private int tipo_vuelo = Vuelos_tbl.INTERNACIONAL;

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
        empty = (TextView) getActivity().findViewById(R.id.empty_message);
        msg  = (TextView) getActivity().findViewById(R.id.act_id);

        //new Delete().from(Vuelo.class).execute();
        internacionales.delegate = this;

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

        //tracker = ((EaaiApplication) getActivity().getApplication()).getTracker();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        //GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
        //tracker.enableAdvertisingIdCollection(true);
        //tracker.setScreenName("Pantalla Vuelos");
        //tracker.send(new HitBuilders.ScreenViewBuilder().build());


       // AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        List<Vuelos_tbl> result = null;
        switch (tab.getPosition()){
            case 0:
                /*tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("Select TAB")
                        .setLabel("LLegadas")
                        .build());
                result = SQLite.select().from(Vuelos_tbl.class)
                        .where(Vuelos_tbl_Table.tipo_vuelo.eq(getTipo_vuelo()))
                        .and(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.LLEGADA)).queryList();
                result = (new Select().from(Vuelo.class)
                        .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                        .and("flujo_vuelo = ? ", String.valueOf(Vuelo.LLEGADA)).execute());

                adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelos_tbl>) result);
                mRecyclerView.setAdapter(adapterVuelos);
                if(adapterVuelos.getItemCount()>0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
                    */
                break;
            case 1:
                /*tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("Select TAB")
                        .setLabel("Salidas")
                        .build());
                result = SQLite.select().from(Vuelos_tbl.class)
                        .where(Vuelos_tbl_Table.tipo_vuelo.eq(getTipo_vuelo()))
                        .and(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.SALIDA)).queryList();
                result = (new Select().from(Vuelo.class)
                        .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                        .and("flujo_vuelo = ? ", String.valueOf(Vuelo.SALIDA)).execute());

                adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelos_tbl>) result);
                mRecyclerView.setAdapter(adapterVuelos);
                if(adapterVuelos.getItemCount()>0)
                    empty.setVisibility(View.GONE);
                else
                    empty.setVisibility(View.VISIBLE);
                     */
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
    public void loadFinish(FlightsEvents events) {
        /*adapterVuelos = new AdapterVuelos(R.layout.row_vuelos,vuelos);
        mRecyclerView.setAdapter(adapterVuelos);
        if(adapterVuelos.getItemCount()>0) {
            empty.setVisibility(View.GONE);
            Vuelos_tbl v = vuelos.get(0);
            msg.setText( getContext().getResources().getString(R.string.act_msg)+" " + v.getMsg());
            pgd.dismissWithAnimation();
        }else {

            List<Vuelos_tbl> result = SQLite.select().from(Vuelos_tbl.class)
                    .where(Vuelos_tbl_Table.tipo_vuelo.eq(getTipo_vuelo()))
                    .and(Vuelos_tbl_Table.flujo_vuelo.eq(Vuelos_tbl.LLEGADA)).queryList();
            adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelos_tbl>) result);
            mRecyclerView.setAdapter(adapterVuelos);
            if (result.size() <= 0){
                empty.setVisibility(View.VISIBLE);
                pgd.dismissWithAnimation();
            }else {
                Vuelos_tbl v = result.get(0);
                msg.setText(v.getMsg());
                pgd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                pgd.setTitleText("¡Oh vaya!")
                        .setConfirmText("OK");
                pgd.setContentText("No fue posible descargar los datos de vuelo, se mostraran datos del: " + v.getLast_update());
                pgd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }
        }*/
        long loadTime = (System.currentTimeMillis() - startTime)/1000;

        //pgd.dismissWithAnimation();
        //Log.d("EDER", String.valueOf(loadTime));

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
                    /*List<Vuelo> result = (new Select().from(Vuelo.class)
                            .where("tipo_vuelo = ? ", String.valueOf(getTipo_vuelo()))
                            .and("flujo_vuelo = ? ", String.valueOf(Vuelo.LLEGADA)).execute());
                    adapterVuelos = new AdapterVuelos(R.layout.row_vuelos, (ArrayList<Vuelo>) result);
                    mRecyclerView.setAdapter(adapterVuelos);
                    if (adapterVuelos.getItemCount() > 0)
                        empty.setVisibility(View.GONE);
                    else
                        empty.setVisibility(View.VISIBLE);
                        */
                }
            }

        }catch (Exception e){
            Log.d("EDER", e.getMessage());
        }
    }


    public void refresh(){
        //new Delete().from(Vuelo.class).execute();

        internacionales = new AsynLLegadasInternacionales();
        internacionales.delegate = this;
        startTime = System.currentTimeMillis();
        internacionales.execute(getContext());
    }
}
