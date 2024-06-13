package com.edxavier.vueloseaai

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.edxavier.vueloseaai.screens.MainScreen
import com.edxavier.vueloseaai.ui.theme.VuelosEAAITheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    lateinit var viewModel: FlightsViewModel
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_VuelosEAAI)
        viewModel = ViewModelProvider(this)[FlightsViewModel::class.java]
        requestInterstitialAds()
        setContent {
            val navController = rememberNavController()
            VuelosEAAITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // WebView()
                    MainScreen(navController = navController, viewModel = viewModel, adSize = getAdSize())
                }
            }
        }
    }
    private fun getAdSize(): AdSize {
        //Determine the screen width to use for the ad width.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()
        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    private fun showInterstitial() {

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
        val adUnitId = resources.getString(R.string.id_interstitial_ad)
        InterstitialAd.load(this, adUnitId, AdRequest.Builder().build(), object:
            InterstitialAdLoadCallback(){
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                mInterstitialAd = p0
            }
        })
    }

    override fun onPause() {
        super.onPause()
        showInterstitial()
    }
}
