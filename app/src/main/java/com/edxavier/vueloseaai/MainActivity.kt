package com.edxavier.vueloseaai

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.edxavier.vueloseaai.core.AdRequestProvider
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.screens.MainScreen
import com.edxavier.vueloseaai.ui.theme.VuelosEAAITheme
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    lateinit var viewModel: FlightsViewModel
    private var mInterstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_VuelosEAAI)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FlightsViewModel::class.java]
        viewModel.onShowInterstitial = { showInterstitial() }
        requestInterstitialAds()
        setContent {
            val navController = rememberNavController()
            VuelosEAAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(navController = navController, viewModel = viewModel, adSize = getAdSize())
                }
            }
        }
    }

    private fun getAdSize(): AdSize {
        val displayMetrics = resources.displayMetrics
        val adWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidthDp)
    }

    fun showInterstitial() {
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
            mInterstitialAd?.show(this)
        }
    }

    private fun requestInterstitialAds() {
        if (isInterstitialLoading) return
        isInterstitialLoading = true

        val adUnitId = if (BuildConfig.DEBUG) {
            resources.getString(R.string.id_interstitial_ad_test)
        } else {
            resources.getString(R.string.id_interstitial_ad)
        }

        InterstitialAd.load(this, adUnitId, AdRequestProvider.get(), object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                isInterstitialLoading = false
                mInterstitialAd = ad
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        requestInterstitialAds()
                    }

                    override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                        mInterstitialAd = null
                        requestInterstitialAds()
                    }
                }
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                isInterstitialLoading = false
            }
        })
    }
}
