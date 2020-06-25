package com.edxavier.vueloseaai.flightDetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.edxavier.vueloseaai.R;
import com.edxavier.vueloseaai.asyncTasks.AsyncTaskContract;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailPresenter;
import com.edxavier.vueloseaai.flightDetail.contracts.DetailView;
import com.edxavier.vueloseaai.flightDetail.event.DetailEvent;
import com.edxavier.vueloseaai.lib.DetectConnection;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightDetailActivity extends AppCompatActivity implements DetailView, AsyncTaskContract {

    @BindView(R.id.conversation_toolbar)
    Toolbar conversationToolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.detailMsg)
    TextView detailMsg;

    @BindView(R.id.mainContainer)
    RelativeLayout mainContainer;

    FirebaseAnalytics analytics;
    Bundle analitycParams = new Bundle();
    DetailPresenter presenter;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progress;
    @BindView(R.id.error_icon)
    AppCompatImageView errorIcon;
    @BindView(R.id.adView)
    AdView adView;

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
        analytics = FirebaseAnalytics.getInstance(this);
        //adViewNative = (NativeExpressAdView) findViewById(R.id.adViewNative);.addTestDevice("45D8AEB3B66116F8F24E001927292BD5")
        //NativeExpressAdView ads = new NativeExpressAdView(this);
        presenter = new DetailPresenterImpl(this);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //ads.setAdSize(new AdSize(280, 132));
        //ads.setAdUnitId("ca-app-pub-9964109306515647/9944122612");
        //ads.loadAd(adRequest);
        //layout.addView(ads);
        setupToolbar();

        Intent intent = getIntent();
        vuelo = intent.getStringExtra("vuelo");
        linea = intent.getStringExtra("aerolinea");
        boolean isPurchased = intent.getBooleanExtra("isPurchased", false);
        if (!isPurchased) {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adView.loadAd(adRequest);
        }

        loadWebview();
        analitycParams.putString("linea", linea);
        analytics.logEvent("activity_details", analitycParams);
        Answers.getInstance().logContentView(new ContentViewEvent().putContentType("Pantalla")
                .putContentName("Detalle de vuelo").putContentId(vuelo).putCustomAttribute("Aerolinea", linea));
    }

    private void loadWebview() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                detailMsg.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                // webView.loadUrl("javascript:document.getElementByClassName('#mainContent_LabelOrigen')");
                webView.setVisibility(View.VISIBLE);
            }
        });

        if (!DetectConnection.isMobileConnected(this) && !DetectConnection.isWifiConnected(this)) {
            Snackbar.make(mainContainer, getResources().getString(R.string.no_connection), Snackbar.LENGTH_LONG).show();
            detailMsg.setText(getResources().getString(R.string.no_connection));
            detailMsg.setTextSize(14);
            detailMsg.setTextColor(Color.RED);
            progress.setVisibility(View.GONE);
            errorIcon.setVisibility(View.VISIBLE);
            //errorIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_signal_wifi_off));
            errorIcon.setImageResource(R.drawable.ic_signal_wifi_off);
            return;
        }
        if (setupURL().length() > 10) {
            webView.loadUrl(setupURL());
        } else {
            detailMsg.setText(getString(R.string.notice_msg));
            detailMsg.setTextSize(14);
            errorIcon.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }

    }


    private String setupURL() {
        String URL = "";
        Date today = new Date();
        SimpleDateFormat dt;
        String vueloNum = vuelo.split(" ")[1];
        switch (linea) {
            case "Avianca":
            case "Avianca Star Alliance":
                dt = new SimpleDateFormat("dd/MM/yyyy");
                URL = AVIANCA + "VLO=" + vueloNum + "&FEC=" + dt.format(today) + "#mainContent_panelUpdateConsultaEquipaje";
                break;
            case "American Airlines":
                dt = new SimpleDateFormat("yyyy,MM,dd");
                URL = AMERICAN + vueloNum + "|" + dt.format(today) + "#mainFlightStatus";
                break;
            case "Copa Airlines":
                dt = new SimpleDateFormat("yyyyMMdd");
                URL = COPA + "trAcid=" + vueloNum + "&trDate=" + dt.format(today) + "#ffStatus";
                break;
            case "Delta Airlines":
                dt = new SimpleDateFormat("yyyy-MM-dd");
                URL = DELTA + "flightNumber=" + vueloNum + "&flightDate=" + dt.format(today) + "#status-header";
                break;
            case "Spirit Airlines":
                dt = new SimpleDateFormat("yyyy-MM-dd");
                URL = SPIRIT;
                break;
            case "Aeromexico":
                dt = new SimpleDateFormat("yyyyMMdd");
                URL = AEROMEXICO + "fn=" + vueloNum + "&depdate=" + dt.format(today);
                break;
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
