package com.edxavier.vueloseaai.core.ui

import android.view.View
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
import androidx.compose.ui.platform.LocalContext
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

    val adUnitId = if (BuildConfig.DEBUG) {
        context.getString(R.string.id_native_ad_test)
    } else {
        context.getString(R.string.id_native_ad)
    }

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember {
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
        val tertiaryColor = MaterialTheme.colorScheme.tertiary.hashCode()
        val surfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant.hashCode()

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
                    val headlineColor = tertiaryColor
                    val bodyColor = surfaceVariantColor
                    NativeAdView(ctx).apply {
                        val contentLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(
                                (12 * density).toInt(),
                                (10 * density).toInt(),
                                (12 * density).toInt(),
                                (10 * density).toInt()
                            )
                        }

                        val iconView = ImageView(ctx).apply {
                            val size = (44 * density).toInt()
                            layoutParams = LinearLayout.LayoutParams(size, size).apply {
                                setMargins(0, 0, (12 * density).toInt(), 0)
                            }
                        }
                        this.iconView = iconView
                        contentLayout.addView(iconView)

                        val textColumn = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                            )
                        }

                        val headlineView = TextView(ctx).apply {
                            setTextColor(headlineColor)
                            textSize = 16f
                        }
                        this.headlineView = headlineView
                        textColumn.addView(headlineView)

                        val bodyView = TextView(ctx).apply {
                            setTextColor(bodyColor)
                            textSize = 12f
                        }
                        this.bodyView = bodyView
                        textColumn.addView(bodyView)

                        contentLayout.addView(textColumn)

                        val ctaButton = Button(ctx).apply {
                            textSize = 11f
                        }
                        val ctaParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply { gravity = android.view.Gravity.CENTER_VERTICAL }
                        this.callToActionView = ctaButton
                        contentLayout.addView(ctaButton, ctaParams)

                        addView(contentLayout)
                        setNativeAd(ad)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
