package com.edxavier.vueloseaai.core.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.AdRequestProvider
import com.google.android.gms.ads.*

@Composable
fun BannerAdView(
    isTest: Boolean = true,
    adSize: AdSize,
) {
    val unitId = if (isTest) {
        stringResource(id = R.string.id_banner_test)
    } else {
        stringResource(id = R.string.id_banner)
    }
    val context = LocalContext.current

    val adView = remember(context, adSize, unitId) {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = unitId
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w("BannerAd", "Failed: ${error.message}, code: ${error.code}")
                }
                override fun onAdLoaded() {
                    Log.i("BannerAd", "Loaded successfully")
                }
            }
            loadAd(AdRequestProvider.get())
        }
    }

    LaunchedEffect(adView) {
        kotlinx.coroutines.delay(60_000L)
        while (true) {
            adView.loadAd(AdRequestProvider.get())
            kotlinx.coroutines.delay(60_000L)
        }
    }

    AndroidView(
        factory = { adView },
        modifier = Modifier.fillMaxWidth()
    )
}
