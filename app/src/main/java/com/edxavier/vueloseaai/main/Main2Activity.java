package com.edxavier.vueloseaai.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.edxavier.vueloseaai.EstacionamientoFragment;
import com.edxavier.vueloseaai.FragmentAduana;
import com.edxavier.vueloseaai.Fragment_vuelos;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBar actionBar;
    NavigationView navigationView;
    Fragment_vuelos frg_vuelos;
   // Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle( navigationView.getMenu().getItem(0).getSubMenu().getItem(0).getTitle());
        }catch (Exception ignored){}
        //tracker = ((EaaiApplication) getApplication()).getTracker();
       // GoogleAnalytics.getInstance(this).reportActivityStart(this);
       // tracker.enableAdvertisingIdCollection(true);
        //tracker.setScreenName("Pantalla Vuelos");
        //tracker.send(new HitBuilders.ScreenViewBuilder().build());

        loadData();
    }


    public void loadData(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        frg_vuelos = new Fragment_vuelos();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, frg_vuelos, "vuelos")
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            //loadData();
            if(frg_vuelos!=null) {
                frg_vuelos.refresh();
                frg_vuelos.setTipo_vuelo(Vuelos_tbl.INTERNACIONAL);
                navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
                actionBar.setTitle(navigationView.getMenu().getItem(0).getSubMenu().getItem(0).getTitle());
            }
            return true;
        }else if(id == R.id.action_rate){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
            return true;
        }else if(id == R.id.action_share){
            try
            { Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Vuelos EAAI");
                String sAux = "\nMe gustaría recomendarte esta aplicación\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+ getPackageName()+" \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Compartir usando"));
            }
            catch(Exception e)
            { //e.toString();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.drawer_internacional:
                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawer(GravityCompat.START);
                if(frg_vuelos!=null) {
                    frg_vuelos.setTipo_vuelo(Vuelos_tbl.INTERNACIONAL);
                }else
                    loadData();
                return true;

            case R.id.drawer_aduana:
                frg_vuelos =null;
                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawer(GravityCompat.START);
                FragmentAduana aduana = new FragmentAduana();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, aduana, "aduana")
                        .commit();
                return true;

            case R.id.drawer_parqueo:
                frg_vuelos =null;
                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawer(GravityCompat.START);
                EstacionamientoFragment estacionamiento = new EstacionamientoFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, estacionamiento, "estacionamiento")
                        .commit();
                return true;


        }
        return true;
    }

}
