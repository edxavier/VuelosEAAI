package com.edxavier.vueloseaai.database.model.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;

import java.util.ArrayList;

/**
 * Created by Eder Xavier Rojas on 27/08/2015.
 */
public class AdapterVuelos extends RecyclerView.Adapter<AdapterVuelos.ViewHolder> {
    private int view;
    private View item;
    private ArrayList<Vuelos_tbl> vuelos ;


    public interface AdapterInterfaceListener {
        void OnAdapterItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextView txt_aerolinea, txt_vuelo, txt_ciudad, txt_hora, txt_estatus;
        ImageView logo;

        public ViewHolder(View viewLayout) {
            super(viewLayout);
            txt_aerolinea = (TextView) viewLayout.findViewById(R.id.aerolinea);
            txt_vuelo = (TextView) viewLayout.findViewById(R.id.vuelo);
            txt_ciudad = (TextView) viewLayout.findViewById(R.id.ciudad);
            txt_hora = (TextView) viewLayout.findViewById(R.id.hora);
            txt_estatus = (TextView) viewLayout.findViewById(R.id.estatus);
            logo = (ImageView) viewLayout.findViewById(R.id.logo_linea);

        }


    }


    public AdapterVuelos(int view, ArrayList<Vuelos_tbl> vuelos) {
        this.view = view;
        this.vuelos = vuelos;

    }



    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

//Metodo donde se pasan los valores de cada fila
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Vuelos_tbl vuelo = vuelos.get(position);
        holder.txt_aerolinea.setText(vuelo.getLinea().trim());
        holder.txt_ciudad.setText(vuelo.getCiudad());
        holder.txt_vuelo.setText(holder.itemView.getResources().getString(R.string.vuelo)+": " + vuelo.getVuelo());
        holder.txt_hora.setText(vuelo.getHora());
        holder.txt_estatus.setText(vuelo.getEstatus());

        if(vuelo.getEstatus().equals("Cancelado")) {
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_red_500));
            holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.cancelado));
        }else if (vuelo.getEstatus().equals("Demorado")) {
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_purple_500));
            holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.demorado));
        }else if (vuelo.getEstatus().equals("Despegó") || vuelo.getEstatus().equals("Arribó")) {
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_green_500));
            if (vuelo.getEstatus().equals("Despegó"))
                holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.despego));
            else if(vuelo.getEstatus().equals("Arribó"))
                holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.arribo));
        }else if (vuelo.getEstatus().equals("Abordando")) {
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_cyan_500));
            holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.abordando));
        }else if (vuelo.getEstatus().equals("Confirmado")) {
            holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.confirmado));
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_teal_500));
        }
        else {
            holder.txt_estatus.setTextColor(holder.itemView.getResources().getColor(R.color.md_black_1000_50));
            holder.txt_estatus.setText(holder.itemView.getResources().getString(R.string.atiempo));
        }


        String v = vuelo.getLinea().replaceAll("^\\s+", "");
        if(v.equals("Copa Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.copa));
        else if(v.equals("Avianca"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.avianca));
        else if(v.equals("American Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.american));
        else if(v.equals("Aeromexico"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.aeromexico));
        else if(v.equals("United Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.continental));
        else if(v.equals("Delta Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.delta));
        else if(v.equals("Spirit Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.spirit));
        else if(v.equals("La Costeña"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.costenia));
        else if(v.equals("Conviasa"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.conviasa));
        else if(v.equals("Cubana de Aviación"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.cubana));
        else if(v.equals("VECA Airlines"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.veca));
        else if(v.equals("Nature Air"))
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.nature));
        else
            holder.logo.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_airplane_take_off_100));

    }

    @Override
    public int getItemCount() {
        if(vuelos!=null)
            return vuelos.size();
        else
            return 0;
    }

}
