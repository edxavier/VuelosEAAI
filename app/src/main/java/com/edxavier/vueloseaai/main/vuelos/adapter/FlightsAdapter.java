package com.edxavier.vueloseaai.main.vuelos.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.FlightsHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {


    private List<Vuelos_tbl> flightsDataset;
    private OnItemClickListener clickListener;

    public FlightsAdapter(List<Vuelos_tbl> flightsDataset, OnItemClickListener clickListener) {
        this.flightsDataset = flightsDataset;
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo_linea)
        ImageView logoLinea;
        @BindView(R.id.vuelo)
        TextView vuelo;
        @BindView(R.id.aerolinea)
        TextView aerolinea;
        @BindView(R.id.ciudad)
        TextView ciudad;
        @BindView(R.id.estatus)
        TextView estatus;
        @BindView(R.id.hora)
        TextView hora;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this,this.view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vuelos, parent, false);
        return new ViewHolder(v);
        //return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Vuelos_tbl vuelo = flightsDataset.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(vuelo);
            }
        });

        holder.aerolinea.setText(vuelo.getLinea().trim());
        holder.ciudad.setText(vuelo.getCiudad());
        holder.vuelo.setText(holder.itemView.getResources().getString(R.string.vuelo)+": " + vuelo.getVuelo());
        holder.hora.setText(vuelo.getHora());
        holder.estatus.setText(vuelo.getEstatus());

        holder.estatus = FlightsHelper.setStatusText(holder.view.getContext(), holder.estatus, vuelo.getEstatus());
        String v = vuelo.getLinea().replaceAll("^\\s+", "");
        Drawable drawable = FlightsHelper.getFlightDrawable(holder.view.getContext(), v);
        holder.logoLinea.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        if(flightsDataset!=null)
            return flightsDataset.size();
        else
            return 0;
    }

    public void setFlightsItems(List<Vuelos_tbl> dataset) {
        flightsDataset.addAll(dataset);
    }
    public void clearFlightsItems() {
        flightsDataset.clear();

    }

}
