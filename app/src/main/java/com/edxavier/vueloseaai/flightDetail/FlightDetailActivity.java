package com.edxavier.vueloseaai.flightDetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edxavier.vueloseaai.EaaiApplication;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.asyncTasks.AsyncTaskContract;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailPresenter;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailView;
import com.edxavier.vueloseaai.flightDetail.di.DetailComponent;
import com.edxavier.vueloseaai.flightDetail.event.DetailEvent;
import com.edxavier.vueloseaai.lib.DetectConnection;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FlightDetailActivity extends AppCompatActivity implements DetailView, AsyncTaskContract {

    @Bind(R.id.adViewNative)
    NativeExpressAdView adViewNative;
    //@Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.conversation_toolbar)
    Toolbar conversationToolbar;

    @Inject
    DetailPresenter presenter;
    @Inject
    FirebaseAnalytics analytics;
    @Bind(R.id.detailMsg)
    TextView detailMsg;
    @Bind(R.id.detail_progress)
    ProgressBar detailProgress;
    @Bind(R.id.webView)
    WebView webView;
    Bundle analitycParams = new Bundle();
    @Bind(R.id.mainContainer)
    RelativeLayout mainContainer;

    private String vuelo, linea;
    private String SPIRIT = "https://www.spirit.com/FlightStatus.aspx";
    private String AEROMEXICO = "http://tracker.flightview.com/customersetup/esAeroMexico/custom/?sf_input=MEX&lang=es&qtype=ByFlight&al=AM&";
    private String DELTA = "http://es.delta.com/flightinfo/viewFlightStatusByOrigin?";
    private String COPA = "https://www.copaair.com/sites/cc/es/flight-information/pages/flight-status.aspx?trQtype=SingleFlight&lc=es&";
    private String AMERICAN = "https://www.aa.com/travelInformation/flights/status/detail?search=AA|";
    private String AVIANCA = "http://serviciosenlinea.avianca.com/EstadoVuelos/ResultadoEstadoVuelos.aspx?idioma=es&pais=NI&";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_detail);
        ButterKnife.bind(this);
        //adViewNative = (NativeExpressAdView) findViewById(R.id.adViewNative);.addTestDevice("45D8AEB3B66116F8F24E001927292BD5")
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewNative.loadAd(adRequest);
        NativeExpressAdView ads = new NativeExpressAdView(this);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    /*
        Log.e("EDER_xdpi", String.valueOf((int) dm.xdpi));
        Log.e("EDER_ydpi", String.valueOf((int) dm.ydpi));
        Log.e("EDER_density", String.valueOf((int) dm.density));
        Log.e("EDER_densityDpi", String.valueOf((int) dm.densityDpi));

        Log.e("EDER_width", String.valueOf(width));
        Log.e("EDER_height", String.valueOf(height));
*/
        ads.setAdSize(new AdSize(280, 132));
        ads.setAdUnitId("ca-app-pub-9964109306515647/9944122612");
        //ads.loadAd(adRequest);
        //layout.addView(ads);
        setupToolbar();
        setupInjection();

        Intent intent = getIntent();
        vuelo = intent.getStringExtra("vuelo");
        linea = intent.getStringExtra("aerolinea");

        loadWebview();
        analitycParams.putString("DetailActivity", "DetailActivity");
        analytics.logEvent("Detail_activity", analitycParams);

    }

    private void loadWebview() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                detailMsg.setVisibility(View.GONE);
                detailProgress.setVisibility(View.GONE);
                // webView.loadUrl("javascript:document.getElementByClassName('#mainContent_LabelOrigen')");
                webView.setVisibility(View.VISIBLE);
            }
        });

        if (!DetectConnection.isMobileConnected(this) && !DetectConnection.isWifiConnected(this)) {
            Snackbar.make(mainContainer, getResources().getString(R.string.no_connection), Snackbar.LENGTH_LONG).show();
            detailMsg.setText(getResources().getString(R.string.no_connection));
            detailMsg.setTextSize(14);
            detailMsg.setTextColor(Color.RED);
            detailProgress.setVisibility(View.GONE);
            return;
        }
        if (setupURL().length() > 10) {
            webView.loadUrl(setupURL());
        } else {
            analitycParams.putString("DetailActivity", "No data avialable");
            analytics.logEvent("error_load_query_page", analitycParams);
            SweetAlertDialog pgd = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            pgd.setTitleText(getResources().getString(R.string.aviso));
            pgd.setContentText(getResources().getString(R.string.notice_msg));
            pgd.setCancelable(false);
            pgd.show();
            detailMsg.setVisibility(View.GONE);
            detailProgress.setVisibility(View.GONE);
        }

    }

    private void setupInjection() {
        EaaiApplication app = (EaaiApplication) getApplication();
        DetailComponent component = app.getDetailComponent(this, this);
        component.inject(this);
    }


    private String setupURL() {
        String URL = "";
        Date today = new Date();
        SimpleDateFormat dt;
        String vueloNum = vuelo.split(" ")[1];
        if (linea.equals("Avianca")) {
            dt = new SimpleDateFormat("dd/MM/yyyy");
            URL = AVIANCA + "VLO=" + vueloNum + "&FEC=" + dt.format(today) + "#mainContent_panelUpdateConsultaEquipaje";
        } else if (linea.equals("American Airlines")) {
            dt = new SimpleDateFormat("yyyy,MM,dd");
            URL = AMERICAN + vueloNum + "|" + dt.format(today) + "#mainFlightStatus";
        } else if (linea.equals("Copa Airlines")) {
            dt = new SimpleDateFormat("yyyyMMdd");
            URL = COPA + "trAcid=" + vueloNum + "&trDate=" + dt.format(today) + "#ffStatus";
        } else if (linea.equals("Delta Airlines")) {
            dt = new SimpleDateFormat("yyyy-MM-dd");
            URL = DELTA + "flightNumber=" + vueloNum + "&flightDate=" + dt.format(today) + "#status-header";
        } else if (linea.equals("Spirit Airlines")) {
            dt = new SimpleDateFormat("yyyy-MM-dd");
            URL = SPIRIT;
        } else if (linea.equals("Aeromexico")) {
            dt = new SimpleDateFormat("yyyyMMdd");
            URL = AEROMEXICO + "fn=" + vueloNum + "&depdate=" + dt.format(today);
        }
        return URL;
    }

    void setupToolbar() {
        setSupportActionBar(conversationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void loadData() {
        presenter.getFlightDetail("");
    }

    @Override
    public void showElements(boolean show) {

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void setContent(DetailEvent event) {

    }

    @Override
    public void onAsynctaskFinish(String event) {

    }
}
