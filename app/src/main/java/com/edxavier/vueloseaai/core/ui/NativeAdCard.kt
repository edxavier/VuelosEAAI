package com.edxavier.vueloseaai.core.ui

import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAdCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density

    val adUnitId = stringResource(
        if (BuildConfig.DEBUG) R.string.id_native_ad_test
        else R.string.id_native_ad
    )

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember(context, adUnitId) {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd?.destroy()
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {}
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()
    }

    DisposableEffect(Unit) {
        adLoader.loadAd(AdRequest.Builder().build())
        onDispose { nativeAd?.destroy() }
    }

    nativeAd?.let { ad ->
        val headlineColor = MaterialTheme.colorScheme.tertiary.toArgb()
        val bodyColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
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

                    NativeAdView(ctx).apply {
                        val contentLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(marginPx, marginPx, marginPx, marginPx)
                        }

                        val iconView = ImageView(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                                setMargins(0, 0, marginPx, 0)
                            }
                        }
                        this.iconView = iconView

                        val textColumn = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                            )
                        }

                        val headlineView = TextView(ctx).apply {
                            setTextColor(headlineColor)
                            textSize = 16f
                            maxLines = 1
                        }
                        this.headlineView = headlineView

                        val bodyView = TextView(ctx).apply {
                            setTextColor(bodyColor)
                            textSize = 12f
                            maxLines = 2
                        }
                        this.bodyView = bodyView

                        textColumn.addView(headlineView)
                        textColumn.addView(bodyView)

                        val ctaButton = Button(ctx).apply {
                            textSize = 11f
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
                        setNativeAd(ad)

                        headlineView.text = ad.headline ?: ""
                        bodyView.text = ad.body ?: ad.advertiser ?: ""
                        ctaButton.text = ad.callToAction ?: ""
                        ctaButton.visibility = if (ad.callToAction != null)
                            android.view.View.VISIBLE
                        else
                            android.view.View.GONE
                        ad.icon?.drawable?.let { iconView.setImageDrawable(it) }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
