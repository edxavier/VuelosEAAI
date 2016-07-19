package com.edxavier.vueloseaai.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edxavier.vueloseaai.EaaiApplication;
import com.edxavier.vueloseaai.EstacionamientoFragment;
import com.edxavier.vueloseaai.FragmentAduana;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.DetectConnection;
import com.edxavier.vueloseaai.main.contracts.MainPresenter;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.di.MainComponent;
import com.edxavier.vueloseaai.main.pageAdapter.MainPagerAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FlightsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView, OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @Bind(R.id.container)
    ViewPager container;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navView;

    ActionBar actionBar;

    @Bind(R.id.adView)
    AdView adView;


    @Inject
    FirebaseAnalytics analytics;
    @Inject
    MainPresenter presenter;
    Fragment[] fragments;
    @Bind(R.id.layout_loading)
    LinearLayout layoutLoading;
    @Bind(R.id.coordinatorContainer)
    CoordinatorLayout coordinatorContainer;

    Bundle analitycParams;
    @Bind(R.id.fragmentContainer)
    FrameLayout fragmentContainer;

    FragmentManager manager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupToolbar();
        //setupPageAdapter();
        setupInjection();
//.addTestDevice("45D8AEB3B66116F8F24E001927292BD5")
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        presenter.onCreate();

        loadData();

        analitycParams = new Bundle();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupInjection() {
        EaaiApplication app = (EaaiApplication) getApplication();
        MainComponent component = app.getMainComponent(this, this, this);
        component.inject(this);
        fragmentContainer.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupToolbar() {

        manager = getSupportFragmentManager();
        if (slidingTabs.getTabCount() <= 0) {
            slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.llegadass)));
            slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.salidas)));
        }
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
        navView.setNavigationItemSelectedListener(this);
        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle(navView.getMenu().getItem(0).getSubMenu().getItem(0).getTitle());
        } catch (Exception ignored) {
        }
    }

    private void setupPageAdapter() {
        FlightsFragment llegadas = new FlightsFragment();
        FlightsFragment salidas = new FlightsFragment();
        Bundle bundl = new Bundle();
        bundl.putInt("direction", Vuelos_tbl.LLEGADA);
        bundl.putBoolean("do_load", true);
        llegadas.setArguments(bundl);

        bundl = new Bundle();
        bundl.putInt("direction", Vuelos_tbl.SALIDA);
        bundl.putBoolean("remote", false);
        salidas.setArguments(bundl);

        fragments = new Fragment[]{llegadas, salidas};
        String[] titles = new String[]{getString(R.string.llegadass), getString(R.string.salidas)};
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(manager, titles, fragments);
        container.setAdapter(pagerAdapter);
        slidingTabs.setupWithViewPager(container);

    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        String[] titles;
        switch (item.getItemId()) {
            case R.id.drawer_internacional:
                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawerLayout.closeDrawer(GravityCompat.START);
                manager.getFragments().clear();
                container.removeAllViews();
                if (slidingTabs.getTabCount() <= 1) {
                    slidingTabs.removeAllTabs();
                    slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.llegadass)));
                    slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.salidas)));
                }
                loadData();
                slidingTabs.setVisibility(View.VISIBLE);
                slidingTabs.setEnabled(true);
                return true;

            case R.id.drawer_aduana:
                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawerLayout.closeDrawer(GravityCompat.START);

                manager.getFragments().clear();
                container.removeAllViews();

                Fragment[] fragmentsa = new Fragment[]{new FragmentAduana()};
                String[] titlesa = new String[]{getString(R.string.aduana)};
                MainPagerAdapter pagerAdaptera = new MainPagerAdapter(getSupportFragmentManager(), titlesa, fragmentsa);

                container.setAdapter(pagerAdaptera);
                //slidingTabs.setupWithViewPager(container);

                analitycParams.putString("park", "app");
                analytics.logEvent("aduana_section", analitycParams);
                return true;

            case R.id.drawer_parqueo:
                analitycParams.putString("park", "app");
                analytics.logEvent("parqueo_section", analitycParams);

                manager.getFragments().clear();
                container.removeAllViews();

                Fragment[] fragmentse = new Fragment[]{new EstacionamientoFragment()};
                String[] titlese = new String[]{getString(R.string.parqueo)};

                MainPagerAdapter pagerAdaptere = new MainPagerAdapter(manager, titlese, fragmentse);
                container.setAdapter(pagerAdaptere);
                //slidingTabs.setupWithViewPager(container);

                item.setChecked(true);
                actionBar.setTitle(item.getTitle());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            container.removeAllViews();
            loadData();
        } else if (id == R.id.action_share) {
            analitycParams.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Share app");
            analytics.logEvent(FirebaseAnalytics.Event.SHARE, analitycParams);

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Vuelos EAAI");
                String sAux = "\nMe gustaría recomendarte esta aplicación\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + " \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Compartir por medio de:"));
            } catch (Exception e) { //e.toString();
            }
        } else if (id == R.id.action_rate) {

            analitycParams.putString("Rate", "Rate app");
            analytics.logEvent("rate_app", analitycParams);

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData() {
        presenter.getFlightsData();
    }

    @Override
    public void showElements(boolean show) {

    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            layoutLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(String error) {
        //Snackbar.make(coordinatorContainer, error, Snackbar.LENGTH_INDEFINITE).show();
        Snackbar snackbar = Snackbar
                .make(coordinatorContainer, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        container.removeAllViews();
                        loadData();
                    }
                });

        snackbar.show();
        showProgress(false);
        setupPageAdapter();
        analitycParams.putString("flights_error", error);
        analytics.logEvent("error_list_flights", analitycParams);
    }

    @Override
    public void setContent(List<Vuelos_tbl> items) {
        setupPageAdapter();
    }

    @Override
    public void onItemClick(Vuelos_tbl vuelo) {
    }


}
