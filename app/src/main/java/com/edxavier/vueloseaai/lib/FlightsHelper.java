package com.edxavier.vueloseaai.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.edxavier.vueloseaai.R;

/**
 * Created by Eder Xavier Rojas on 28/06/2016.
 */
public class FlightsHelper {

    public static Drawable getFlightDrawable(Context context, String flight){
        Drawable drawable = null;
        if(flight.equals("Copa Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.copa);
        else if(flight.equals("Avianca"))
            drawable = context.getResources().getDrawable(R.drawable.avianca);
        else if(flight.equals("American Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.american);
        else if(flight.equals("Aeromexico"))
            drawable = context.getResources().getDrawable(R.drawable.aeromexico);
        else if(flight.equals("United Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.continental);
        else if(flight.equals("Delta Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.delta);
        else if(flight.equals("Spirit Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.spirit);
        else if(flight.equals("La Costeña"))
            drawable = context.getResources().getDrawable(R.drawable.costenia);
        else if(flight.equals("Conviasa"))
            drawable = context.getResources().getDrawable(R.drawable.conviasa);
        else if(flight.equals("Cubana de Aviación"))
            drawable = context.getResources().getDrawable(R.drawable.cubana);
        else if(flight.equals("VECA Airlines"))
            drawable = context.getResources().getDrawable(R.drawable.veca);
        else if(flight.equals("Nature Air"))
            drawable = context.getResources().getDrawable(R.drawable.nature);
        else if(flight.equals("Avianca Star Alliance"))
            drawable = context.getResources().getDrawable(R.drawable.start_alliance);
        else if(flight.equals("Volaris"))
            drawable = context.getResources().getDrawable(R.drawable.volaris);
        else
            drawable = context.getResources().getDrawable(R.drawable.ic_airplane_take_off_100);

        return drawable;
    }

    public static TextView setStatusText(Context context, TextView textView, String status){
        TextView statusText = textView;
        if(status.equals("Cancelado")) {
            statusText.setTextColor(context.getResources().getColor(R.color.md_red_700));
            statusText.setText(context.getResources().getString(R.string.cancelado));
        }else if (status.equals("Demorado")) {
            statusText.setTextColor(context.getResources().getColor(R.color.md_purple_700));
            statusText.setText(context.getResources().getString(R.string.demorado));
        }else if (status.equals("Despegó") || status.equals("Arribó")) {
            statusText.setTextColor(context.getResources().getColor(R.color.md_green_700));
            if (status.equals("Despegó"))
                statusText.setText(context.getResources().getString(R.string.despego));
            else if(status.equals("Arribó"))
                statusText.setText(context.getResources().getString(R.string.arribo));
        }else if (status.equals("Abordando")) {
            statusText.setTextColor(context.getResources().getColor(R.color.md_cyan_700));
            statusText.setText(context.getResources().getString(R.string.abordando));
        }else if (status.equals("Confirmado")) {
            statusText.setText(context.getResources().getString(R.string.confirmado));
            statusText.setTextColor(context.getResources().getColor(R.color.md_teal_700));
        }
        else {
            statusText.setTextColor(context.getResources().getColor(R.color.md_black_1000_75));
            statusText.setText(context.getResources().getString(R.string.atiempo));
        }
        return statusText;
    }


}
