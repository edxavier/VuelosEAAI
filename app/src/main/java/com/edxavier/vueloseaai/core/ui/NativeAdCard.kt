package com.edxavier.vueloseaai.core.ui

import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.BuildConfig
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.core.AdRequestProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier,
    onAdFailed: () -> Unit = {}
) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density

    val adUnitId = stringResource(
        if (BuildConfig.DEBUG) R.string.id_native_ad_test
        else R.string.id_native_ad
    )

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var hasFailed by remember { mutableStateOf(false) }

    LaunchedEffect(hasFailed) {
        if (hasFailed) onAdFailed()
    }

    val adLoader = remember(context, adUnitId) {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd?.destroy()
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    hasFailed = true
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()
    }

    DisposableEffect(Unit) {
        adLoader.loadAd(AdRequestProvider.get())
        onDispose { nativeAd?.destroy() }
    }

    nativeAd?.let { ad ->
        val headlineColor = MaterialTheme.colorScheme.tertiary.toArgb()
        val bodyColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
        val ctaBgColor = MaterialTheme.colorScheme.primary.toArgb()
        val ctaTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()

        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(horizontal = 6.dp, vertical = 2.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            AndroidView(
                factory = { ctx ->
                    val iconSize = (48 * density).toInt()
                    val marginPx = (12 * density).toInt()
                    val minHeightPx = (72 * density).toInt()

                    NativeAdView(ctx).apply {
                        val contentLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(marginPx, marginPx, marginPx, marginPx)
                            minimumHeight = minHeightPx
                        }

                        val iconView = ImageView(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                                gravity = android.view.Gravity.CENTER_VERTICAL
                                setMargins(0, 0, marginPx, 0)
                            }
                        }
                        this.iconView = iconView

                        val textColumn = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                            )
                            gravity = android.view.Gravity.CENTER_VERTICAL
                        }

                        val headlineView = TextView(ctx).apply {
                            setTextColor(headlineColor)
                            textSize = 16f
                            maxLines = 1
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                        this.headlineView = headlineView

                        val bodyView = TextView(ctx).apply {
                            setTextColor(bodyColor)
                            textSize = 12f
                            maxLines = 2
                            typeface = android.graphics.Typeface.DEFAULT
                        }
                        this.bodyView = bodyView

                        textColumn.addView(headlineView)
                        textColumn.addView(bodyView)

                        val ctaButton = Button(ctx).apply {
                            textSize = 11f
                            setTextColor(ctaTextColor)
                            setBackgroundColor(ctaBgColor)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                gravity = android.view.Gravity.CENTER_VERTICAL
                                setMargins(marginPx, 0, 0, 0)
                            }
                        }
                        this.callToActionView = ctaButton

                        contentLayout.addView(iconView)
                        contentLayout.addView(textColumn)
                        contentLayout.addView(ctaButton)

                        addView(contentLayout)

                        headlineView.text = ad.headline ?: ""
                        bodyView.text = ad.body ?: ad.advertiser ?: ""
                        ctaButton.text = ad.callToAction ?: ""
                        ctaButton.visibility = if (ad.callToAction != null)
                            android.view.View.VISIBLE
                        else
                            android.view.View.GONE
                        ad.icon?.drawable?.let { iconView.setImageDrawable(it) }

                        setNativeAd(ad)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
