package com.edxavier.vueloseaai.core.ui

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import androidx.core.graphics.toColorInt

private fun starString(rating: Double): String {
    val full = rating.toInt().coerceIn(0, 5)
    val empty = (5 - full).coerceAtLeast(0)
    return "\u2605".repeat(full) + "\u2606".repeat(empty)
}

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
                    .setRequestCustomMuteThisAd(true)
                    .build()
            )
            .build()
    }

    DisposableEffect(Unit) {
        adLoader.loadAd(AdRequestProvider.get())
        onDispose { nativeAd?.destroy() }
    }

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
        if (nativeAd == null) {
            val shimmerTransition = rememberInfiniteTransition(label = "adShimmer")
            val alpha by shimmerTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.7f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            val shimmerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(shimmerColor)
            )
        }

        nativeAd?.let { ad ->
            val headlineColor = MaterialTheme.colorScheme.tertiary.toArgb()
            val bodyColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
            val ctaBgColor = MaterialTheme.colorScheme.surfaceVariant.toArgb()
            val ctaTextColor = MaterialTheme.colorScheme.primary.toArgb()
            val starColor = "#FFA000".toColorInt()

            AndroidView(
                factory = { ctx ->
                    val iconSize = (40 * density).toInt()
                    val marginPx = (10 * density).toInt()
                    val smallMargin = (4 * density).toInt()

                    NativeAdView(ctx).apply {
                        val rootLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(marginPx, marginPx, marginPx, marginPx)
                        }

                        // Row 1: icon | [headline + stars]
                        val topRow = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        val iconView = ImageView(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                                gravity = android.view.Gravity.TOP
                                setMargins(0, 0, marginPx, 0)
                            }
                        }
                        this.iconView = iconView

                        val textArea = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                            )
                        }

                        val headlineStarsRow = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                        }

                        val headlineView = TextView(ctx).apply {
                            setTextColor(headlineColor)
                            textSize = 16f
                            maxLines = 1
                            ellipsize = android.text.TextUtils.TruncateAt.END
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                        this.headlineView = headlineView
                        textArea.addView(headlineView)

                        val starRating = ad.starRating
                        if (starRating != null && starRating > 0) {
                            val starsView = TextView(ctx).apply {
                                text = starString(starRating)
                                setTextColor(starColor)
                                textSize = 12f
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    topMargin = smallMargin
                                }
                            }
                            textArea.addView(starsView)
                        }
                        topRow.addView(iconView)
                        topRow.addView(textArea)
                        rootLayout.addView(topRow)

                        // Row 2: body (full width)
                        val bodyView = TextView(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                topMargin = smallMargin
                            }
                            setTextColor(bodyColor)
                            textSize = 13f
                            maxLines = 2
                            ellipsize = android.text.TextUtils.TruncateAt.END
                        }
                        this.bodyView = bodyView
                        rootLayout.addView(bodyView)

                        // Row 3: CTA full width, compacto
                        val ctaView = TextView(ctx).apply {
                            textSize = 11f
                            gravity = android.view.Gravity.CENTER
                            setTextColor(ctaTextColor)
                            setClickable(true)
                            setFocusable(true)
                            background = android.graphics.drawable.GradientDrawable().apply {
                                setColor(ctaBgColor)
                                cornerRadius = 8 * density
                            }
                            setPadding(marginPx, marginPx, marginPx, marginPx)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                topMargin = smallMargin
                            }
                        }
                        this.callToActionView = ctaView
                        ctaView.text = ad.callToAction ?: ""
                        ctaView.visibility = if (ad.callToAction != null)
                            android.view.View.VISIBLE
                        else
                            android.view.View.GONE
                        rootLayout.addView(ctaView)

                        addView(rootLayout)

                        // AdChoices SDK + badge "Anuncio" (top-right)
                        val adChoices = com.google.android.gms.ads.nativead.AdChoicesView(ctx)
                        val adChoicesSize = (20 * density).toInt()
                        adChoices.layoutParams = LinearLayout.LayoutParams(adChoicesSize, adChoicesSize)

                        val adBadge = LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                android.view.Gravity.TOP or android.view.Gravity.END
                            ).apply {
                                setMargins(0, smallMargin, smallMargin, 0)
                            }
                            val label = TextView(ctx).apply {
                                text = "Ad"
                                textSize = 9f
                                setTextColor("#999999".toColorInt())
                            }
                            addView(label)
                            addView(adChoices)
                        }
                        addView(adBadge)

                        headlineView.text = ad.headline ?: ""
                        bodyView.text = ad.body ?: ad.advertiser ?: ""
                        ad.icon?.drawable?.let { iconView.setImageDrawable(it) }

                        setNativeAd(ad)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
