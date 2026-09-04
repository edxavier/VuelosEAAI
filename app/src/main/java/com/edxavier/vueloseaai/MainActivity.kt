package com.edxavier.vueloseaai

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.edxavier.vueloseaai.core.AdRequestProvider
import com.edxavier.vueloseaai.core.GoogleMobileAdsConsentManager
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.screens.MainScreen
import com.edxavier.vueloseaai.ui.theme.VuelosEAAITheme
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.RequestConfiguration
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdPreloader
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    lateinit var viewModel: FlightsViewModel
    private lateinit var consentManager: GoogleMobileAdsConsentManager
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_App_Starting)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FlightsViewModel::class.java]
        viewModel.onShowInterstitial = { showInterstitial() }

        consentManager = GoogleMobileAdsConsentManager(this)
        consentManager.gatherConsent(this) { error ->
            if (error != null) {
                Log.w("AdMob", "${error.errorCode}: ${error.message}")
            }
            if (consentManager.canRequestAds) {
                initializeMobileAdsSdk()
            }
        }

        if (consentManager.canRequestAds) {
            initializeMobileAdsSdk()
        }

        setContent {
            val navController = rememberNavController()
            VuelosEAAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val appId = getString(R.string.admob_app_id)
            val requestConfig = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("2391D45C5FBB101C3B5A692B3E866DFB"))
                .build()
            val config = InitializationConfig.Builder(appId)
                .setRequestConfiguration(requestConfig)
                .build()

            MobileAds.initialize(this@MainActivity, config) {
                (application as? BaseApp)?.isAdsInitialized = true
                requestInterstitialAds()
                (application as? BaseApp)?.appOpenManager?.fetchAd()
            }
        }
    }

    fun showInterstitial() {
        if (!(application as BaseApp).isAdsInitialized) return
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("EaaiPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val ne = sharedPreferences.getInt("exec_count", 0)
        editor.putInt("exec_count", ne + 1)
        editor.apply()
        if (ne + 1 >= sharedPreferences.getInt("show_after", 2)) {
            editor.putInt("exec_count", 0)
            editor.apply()
            val min = 1
            val max = 3
            val randomValue = Random.nextInt(min, max + 1)
            editor.putInt("show_after", randomValue)
            editor.apply()
            
            val adUnitId = if (BuildConfig.DEBUG) {
                resources.getString(R.string.id_interstitial_ad_test)
            } else {
                resources.getString(R.string.id_interstitial_ad)
            }
            
            val ad = InterstitialAdPreloader.pollAd(adUnitId)
            ad?.let {
                it.adEventCallback = object : InterstitialAdEventCallback {
                    override fun onAdDismissedFullScreenContent() {
                        // Preloader automatically refills the cache
                    }
                }
                it.show(this@MainActivity)
            }
        }
    }

    private fun requestInterstitialAds() {
        val adUnitId = if (BuildConfig.DEBUG) {
            resources.getString(R.string.id_interstitial_ad_test)
        } else {
            resources.getString(R.string.id_interstitial_ad)
        }
        
        val preloadConfig = PreloadConfiguration(AdRequestProvider.get(adUnitId))
        InterstitialAdPreloader.start(adUnitId, preloadConfig)
    }
}
