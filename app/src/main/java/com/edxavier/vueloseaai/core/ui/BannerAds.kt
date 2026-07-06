package com.edxavier.vueloseaai.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.R
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
        }
    }

    LaunchedEffect(adView) {
        while (true) {
            adView.loadAd(AdRequest.Builder().build())
            kotlinx.coroutines.delay(60_000L)
        }
    }

    AndroidView(
        factory = { adView },
        modifier = Modifier.fillMaxWidth()
    )
}
