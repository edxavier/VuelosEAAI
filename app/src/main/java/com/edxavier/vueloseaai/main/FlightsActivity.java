package com.edxavier.vueloseaai.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.edxavier.vueloseaai.BuildConfig;
import com.edxavier.vueloseaai.EstacionamientoFragment;
import com.edxavier.vueloseaai.FragmentAduana;
import com.edxavier.vueloseaai.MainActivity;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.database.model.Vuelos_tbl;
import com.edxavier.vueloseaai.lib.MySnackbar;
import com.edxavier.vueloseaai.main.contracts.MainPresenter;
import com.edxavier.vueloseaai.main.contracts.MainView;
import com.edxavier.vueloseaai.main.pageAdapter.MainPagerAdapter;
import com.edxavier.vueloseaai.main.vuelos.adapter.OnItemClickListener;
import com.edxavier.vueloseaai.main.vuelos.ui.FlightsFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlightsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView, OnItemClickListener, BillingProcessor.IBillingHandler {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.loading_message)
    TextView loadingMessage;
    @BindView(R.id.layout_loading)
    LinearLayout layoutLoading;
    @BindView(R.id.coordinatorContainer)
    CoordinatorLayout coordinatorContainer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    MainPresenter presenter;
    private FragmentManager manager;
    private ActionBar actionBar;
    private Fragment[] fragments;
    private FirebaseAnalytics analytics;
    public BillingProcessor bp;
    public static String PRODUCT = "remove_ads";
    private InterstitialAd mInterstitialAd;
    private MainPagerAdapter pagerAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        ButterKnife.bind(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstical));

        bp = new BillingProcessor(FlightsActivity.this, BuildConfig.APP_BILLING_PUB_KEY, BuildConfig.MERCHANT_ID, this);
        analytics = FirebaseAnalytics.getInstance(this);

        setupToolbar();
        setupPageAdapter();
        showBanner();
    }

    private void showBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                if(pagerAdapter!=null){
                    try {
                        ((FlightsFragment) pagerAdapter.getItem(0)).setRecyclerPadding(110);
                        ((FlightsFragment) pagerAdapter.getItem(1)).setRecyclerPadding(110);
                    }catch (Exception ignored){}
                }
            }
        });
        adView.loadAd(adRequest);
        //presenter.onCreate();
    }

    //public boolean isAdVisible(){
      //  return adView.getVisibility() == View.VISIBLE;
    //}

    @Override
    protected void onDestroy() {
        //presenter.onDestroy();
        super.onDestroy();
    }

    private void setupToolbar() {

        manager = getSupportFragmentManager();
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).setChecked(true);
        navView.setNavigationItemSelectedListener(this);
        if (slidingTabs.getTabCount() <= 0) {
            slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.llegadass)));
            slidingTabs.addTab(slidingTabs.newTab().setText(getResources().getString(R.string.salidas)));
        }
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
        pagerAdapter = new MainPagerAdapter(manager, titles, fragments);
        container.setAdapter(pagerAdapter);
        slidingTabs.setupWithViewPager(container);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //navView.getMenu().getItem(0).setChecked(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        String[] titles;
        switch (item.getItemId()) {
            case R.id.drawer_internacional:
                if(!bp.isPurchased(FlightsActivity.PRODUCT))
                    requestAds();
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
                setupPageAdapter();
                slidingTabs.setVisibility(View.VISIBLE);
                slidingTabs.setEnabled(true);
                if(adView.getVisibility() == View.VISIBLE){
                    if(pagerAdapter!=null){
                        ((FlightsFragment) pagerAdapter.getItem(0)).setRecyclerPadding(100);
                        ((FlightsFragment) pagerAdapter.getItem(1)).setRecyclerPadding(100);
                    }
                }

                return true;

            case R.id.drawer_aduana:
                if(!bp.isPurchased(FlightsActivity.PRODUCT))
                    requestAds();
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

                analytics.logEvent("aduana_section", null);
                return true;

            case R.id.drawer_parqueo:
                analytics.logEvent("parqueo_section", null);
                if(!bp.isPurchased(FlightsActivity.PRODUCT))
                    requestAds();
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

            case R.id.ac_drawer_issues:
                if(!bp.isPurchased(FlightsActivity.PRODUCT))
                    requestAds();
                //bp.consumePurchase(PRODUCT);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","edxavier05@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT   , "Escriba sus comentarios, sugerencias, reporte de error o peticiones");

                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                analytics.logEvent("ac_report_issue", null);
                break;
            case R.id.ac_drawer_remove_ads:
                analytics.logEvent("ac_remove_ads", null);
                bp.purchase(this, PRODUCT);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

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
        if (id == R.id.action_share) {
            analytics.logEvent("ac_drawer_share", null);
            Answers.getInstance().logShare(new ShareEvent()
                    .putMethod("Toolbar")
                    .putContentName("Share app")
                    .putContentType("app")
                    .putContentId("601072000245858305"));
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
            Answers.getInstance().logRating(new RatingEvent());
            analytics.logEvent("ac_drawer_rate", null);
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
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
        //presenter.getFlightsData();
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
    }

    @Override
    public void setContent(List<Vuelos_tbl> items) {
        setupPageAdapter();
    }

    @Override
    public void onItemClick(Vuelos_tbl vuelo) {
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        adView.setVisibility(View.GONE);
        Answers.getInstance().logPurchase(new PurchaseEvent());
        hideItem();
        MySnackbar.info(coordinatorContainer, getString(R.string.purchased_msg), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok), v -> {})
                .show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        MySnackbar.alert(coordinatorContainer, getString(R.string.purchased_err_msg), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.ok), v -> {})
                .show();
    }

    @Override
    public void onBillingInitialized() {
        if(!bp.isPurchased(FlightsActivity.PRODUCT)) {
            showBanner();
            loadInterstical();
            //requestAds();
        }else
            hideItem();
    }
    private void hideItem() {
        Menu nav_Menu = navView.getMenu();
        nav_Menu.findItem(R.id.ac_drawer_remove_ads).setVisible(false);
    }


    public void requestAds() {

        int ne =  Prefs.getInt("num_show_readings", 0);
        Prefs.putInt("num_show_readings", ne + 1);

        if(Prefs.getInt("num_show_readings", 0) >= Prefs.getInt("show_after", 8)) {
            Prefs.putInt("num_show_readings", 0);
            Random r = new Random();
            int Low = 8;int High = 12;
            int rnd = r.nextInt(High-Low) + Low;
            Prefs.putInt("show_after", rnd);
            if(mInterstitialAd.isLoaded()) {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Cargando publicidad...");
                pDialog.setCancelable(false);
                try {
                    Observable.interval(1, TimeUnit.MILLISECONDS).take(1800)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                    },
                                    throwable -> {
                                    },() -> {
                                        pDialog.dismissWithAnimation();
                                        mInterstitialAd.show();
                                        Answers.getInstance().logCustom(new CustomEvent("Admob")
                                                .putCustomAttribute("Show", "Insterstical"));
                                    });
                }catch (Exception ignored){}
                pDialog.show();

            }
        }
    }

    public void loadInterstical(){
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("0B307F34E3DDAF6C6CAB28FAD4084125")
                //.addTestDevice("B0FF48A19BF36BD2D5DCD62163C64F45")
                .build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if(i == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Answers.getInstance().logCustom(new CustomEvent("Admob")
                            .putCustomAttribute("AdFailedToLoad", "ERROR_CODE_INTERNAL_ERROR"));
                }if(i == AdRequest.ERROR_CODE_INVALID_REQUEST ) {
                    Answers.getInstance().logCustom(new CustomEvent("Admob")
                            .putCustomAttribute("AdFailedToLoad", "ERROR_CODE_INVALID_REQUEST"));
                }if(i == AdRequest.ERROR_CODE_NETWORK_ERROR ) {
                    Answers.getInstance().logCustom(new CustomEvent("Admob")
                            .putCustomAttribute("AdFailedToLoad", "ERROR_CODE_NETWORK_ERROR"));
                }if(i == AdRequest.ERROR_CODE_NO_FILL ) {
                    Answers.getInstance().logCustom(new CustomEvent("Admob")
                            .putCustomAttribute("AdFailedToLoad", "ERROR_CODE_NO_FILL"));
                }
                try {
                    Observable.interval(1, TimeUnit.SECONDS).take(4)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                    },
                                    throwable -> {
                                    }, () -> {
                                        loadInterstical();
                                    });
                }catch (Exception ignored){}
            }
        });
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

}
