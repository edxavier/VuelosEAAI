package com.edxavier.vueloseaai.core.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.data.FlightsViewModel
import com.google.android.gms.ads.*

@Composable
fun BannerAdView(
    isTest: Boolean = true,
    adSize: AdSize,
) {
    val unitId = if (isTest) stringResource(id = R.string.id_banner_test) else stringResource(
        id = R.string.id_banner
    )
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = unitId
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}