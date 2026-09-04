package com.edxavier.vueloseaai.core.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.R
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError

@Composable
fun BannerAdView(
    isTest: Boolean = true,
    adSize: AdSize? = null
) {
    val context = LocalContext.current
    val isInitialized = (context.applicationContext as? com.edxavier.vueloseaai.BaseApp)?.isAdsInitialized ?: false
    
    if (!isInitialized) return

    val unitId = if (isTest) {
        stringResource(id = R.string.id_banner_test)
    } else {
        stringResource(id = R.string.id_banner)
    }

    val finalAdSize = remember(adSize, context) {
        if (adSize != null) {
            adSize
        } else {
            val adWidth = context.resources.displayMetrics.widthPixels
            val density = context.resources.displayMetrics.density
            val adWidthDp = (adWidth / density).toInt()
            AdSize.getLargeAnchoredAdaptiveBannerAdSize(context, adWidthDp)
        }
    }

    val adView = remember(context, finalAdSize, unitId) {
        AdView(context).apply {
            val adRequest = BannerAdRequest.Builder(unitId, finalAdSize).build()
            loadAd(adRequest, object : AdLoadCallback<BannerAd> {
                override fun onAdLoaded(ad: BannerAd) {
                    Log.i("BannerAd", "Loaded")
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w("BannerAd", "Failed: ${error.message}, code: ${error.code}")
                }
            })
        }
    }

    AndroidView(
        factory = { adView },
        modifier = Modifier.fillMaxWidth()
    )
}
