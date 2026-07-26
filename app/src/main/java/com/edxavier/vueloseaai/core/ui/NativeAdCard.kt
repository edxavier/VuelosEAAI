package com.edxavier.vueloseaai.core.ui

import android.graphics.Outline
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
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
                Log.d("NativeAd", "Mediation winner: ${ad.responseInfo?.mediationAdapterClassName}")
                nativeAd?.destroy()
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w("NativeAd", "Failed: ${error.message}, code: ${error.code}")
                    hasFailed = true
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .setRequestCustomMuteThisAd(true)
                    .setVideoOptions(
                        VideoOptions.Builder()
                            .setStartMuted(true)
                            .build()
                    )
                    .build()
            )
            .build()
    }

    DisposableEffect(Unit) {
        adLoader.loadAd(AdRequestProvider.get())
        onDispose { nativeAd?.destroy() }
    }

    val cardShape = RoundedCornerShape(12.dp)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .padding(horizontal = 6.dp, vertical = 3.dp),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        if (nativeAd == null) {
            NativeAdShimmer(cardShape)
        }

        nativeAd?.let { ad ->
            key(ad.hashCode()) {
                val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
                val secondaryColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
                val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f).toArgb()
                val ctaBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f).toArgb()
                val ctaTextColor = MaterialTheme.colorScheme.primary.toArgb()
                val adBadgeBg = MaterialTheme.colorScheme.surfaceVariant.toArgb()
                val adBadgeText = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f).toArgb()
                val starColor = "#FFB800".toColorInt()
                val cardBgColor = MaterialTheme.colorScheme.surface.toArgb()

                AndroidView(
                    factory = { ctx ->
                        val paddingPx = (12 * density).toInt()
                        val gapXs = (4 * density).toInt()
                        val gapSm = (6 * density).toInt()
                        val gapMd = (8 * density).toInt()
                        val mediaSize = (120 * density).toInt()
                        val iconSize = (36 * density).toInt()
                        val hasMedia = ad.mediaContent != null
                        val hasAdvertiser = !ad.advertiser.isNullOrBlank()
                        val hasBody = !ad.body.isNullOrBlank()
                        val starRating = ad.starRating
                        val hasStars = (starRating ?: 0.0) > 0
                        val hasCTA = !ad.callToAction.isNullOrBlank()

                        NativeAdView(ctx).apply {
                            setBackgroundColor(cardBgColor)

                            val content = LinearLayout(ctx).apply {
                                orientation = LinearLayout.VERTICAL
                                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
                            }

                            val topRow = LinearLayout(ctx).apply {
                                orientation = LinearLayout.HORIZONTAL
                            }

                            // --- Left: Media (if present) ---
                            if (hasMedia) {
                                val mediaView = MediaView(ctx).apply {
                                    layoutParams = LinearLayout.LayoutParams(mediaSize, mediaSize).apply {
                                        setMargins(0, 0, gapMd, 0)
                                    }
                                    clipToOutline = true
                                    outlineProvider = object : ViewOutlineProvider() {
                                        override fun getOutline(view: View, outline: Outline) {
                                            outline.setRoundRect(0, 0, view.width, view.height, 8 * density)
                                        }
                                    }
                                }
                                this@apply.mediaView = mediaView
                                topRow.addView(mediaView)
                            }

                            // --- Right: Text column ---
                            val textColumn = LinearLayout(ctx).apply {
                                orientation = LinearLayout.VERTICAL
                                layoutParams = LinearLayout.LayoutParams(
                                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                                )
                            }

                            if (hasAdvertiser) {
                                val advertiserView = TextView(ctx).apply {
                                    text = ad.advertiser
                                    setTextColor(mutedColor)
                                    textSize = 10f
                                    maxLines = 1
                                    ellipsize = android.text.TextUtils.TruncateAt.END
                                }
                                textColumn.addView(advertiserView)
                            }

                            val headlineView = TextView(ctx).apply {
                                text = ad.headline ?: ""
                                setTextColor(onSurfaceColor)
                                textSize = 15f
                                maxLines = 3
                                ellipsize = android.text.TextUtils.TruncateAt.END
                                typeface = android.graphics.Typeface.DEFAULT_BOLD
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    topMargin = if (hasAdvertiser) gapXs else 0
                                }
                            }
                            this@apply.headlineView = headlineView
                            textColumn.addView(headlineView)

                            if (hasStars) {
                                val starsView = TextView(ctx).apply {
                                    text = starString(starRating!!)
                                    setTextColor(starColor)
                                    textSize = 11f
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        topMargin = gapXs
                                    }
                                }
                                textColumn.addView(starsView)
                            }

                            if (hasBody) {
                                val bodyView = TextView(ctx).apply {
                                    text = ad.body
                                    setTextColor(secondaryColor)
                                    textSize = 13f
                                    maxLines = 4
                                    ellipsize = android.text.TextUtils.TruncateAt.END
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        topMargin = gapSm
                                    }
                                }
                                this@apply.bodyView = bodyView
                                textColumn.addView(bodyView)
                            }

                            if (hasCTA) {
                                val ctaView = TextView(ctx).apply {
                                    text = ad.callToAction
                                    textSize = 12f
                                    gravity = Gravity.CENTER
                                    setTextColor(ctaTextColor)
                                    setClickable(true)
                                    setFocusable(true)
                                    background = android.graphics.drawable.GradientDrawable().apply {
                                        setColor(ctaBgColor)
                                        cornerRadius = 16 * density
                                    }
                                    setPadding(
                                        (16 * density).toInt(),
                                        (6 * density).toInt(),
                                        (16 * density).toInt(),
                                        (6 * density).toInt()
                                    )
                                    maxLines = 1
                                    ellipsize = android.text.TextUtils.TruncateAt.END
                                }
                                this@apply.callToActionView = ctaView
                                val ctaContainer = LinearLayout(ctx).apply {
                                    gravity = Gravity.END
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        topMargin = gapMd
                                    }
                                }
                                ctaContainer.addView(ctaView)
                                textColumn.addView(ctaContainer)
                            }

                            topRow.addView(textColumn)
                            content.addView(topRow)
                            addView(content)

                            // --- Overlay: Ad badge top-right ---
                            val adChoices = AdChoicesView(ctx)
                            val adChoicesSize = (16 * density).toInt()
                            adChoices.layoutParams = LinearLayout.LayoutParams(adChoicesSize, adChoicesSize)

                            val adBadge = LinearLayout(ctx).apply {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                                layoutParams = FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    Gravity.TOP or Gravity.END
                                ).apply {
                                    setMargins(0, gapXs, gapXs, 0)
                                }
                                val label = TextView(ctx).apply {
                                    text = "Ad"
                                    textSize = 9f
                                    setTextColor(adBadgeText)
                                    gravity = Gravity.CENTER
                                    background = android.graphics.drawable.GradientDrawable().apply {
                                        setColor(adBadgeBg)
                                        cornerRadius = 3 * density
                                    }
                                    setPadding(
                                        (5 * density).toInt(),
                                        (2 * density).toInt(),
                                        (5 * density).toInt(),
                                        (2 * density).toInt()
                                    )
                                }
                                addView(label)
                                val dot = Space(ctx).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        (2 * density).toInt(), 0
                                    )
                                }
                                addView(dot)
                                addView(adChoices)
                            }
                            addView(adBadge)

                            if (!hasMedia && ad.icon != null) {
                                val iconView = ImageView(ctx).apply {
                                    layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                                        setMargins(0, 0, gapMd, 0)
                                    }
                                    clipToOutline = true
                                    outlineProvider = object : ViewOutlineProvider() {
                                        override fun getOutline(view: View, outline: Outline) {
                                            outline.setRoundRect(0, 0, view.width, view.height, iconSize / 2f)
                                        }
                                    }
                                }
                                this@apply.iconView = iconView
                                topRow.addView(iconView, 0)
                            }

                            setNativeAd(ad)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun NativeAdShimmer(cardShape: RoundedCornerShape) {
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
    val shimmer = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row {
            Box(
                Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(shimmer)
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(shimmer)
            )
            Box(
                Modifier
                    .width(76.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(shimmer)
            )
        }
    }
}
