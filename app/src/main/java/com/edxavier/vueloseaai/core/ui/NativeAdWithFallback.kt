package com.edxavier.vueloseaai.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.edxavier.vueloseaai.BuildConfig
import com.google.android.gms.ads.AdSize

@Composable
fun NativeAdWithFallback(
    modifier: Modifier = Modifier,
) {
    var showBanner by rememberSaveable { mutableStateOf(false) }

    if (showBanner) {
        BannerAdView(
            adSize = AdSize.MEDIUM_RECTANGLE,
            isTest = BuildConfig.DEBUG,
        )
    } else {
        NativeAdCard(
            modifier = modifier,
            onAdFailed = { showBanner = true },
        )
    }
}
