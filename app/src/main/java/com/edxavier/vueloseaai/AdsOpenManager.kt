package com.edxavier.vueloseaai

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.edxavier.vueloseaai.core.AdRequestProvider
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdPreloader
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration

class AdsOpenManager(private val myApplication: BaseApp) : DefaultLifecycleObserver, Application.ActivityLifecycleCallbacks {
    private var isShowingAd = false
    private var currentActivity: Activity? = null

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        showAdIfAvailable()
    }

    /** Request an ad  */
    fun fetchAd() {
        if (!myApplication.isAdsInitialized) return
        val adUnitId = if (BuildConfig.DEBUG) {
            myApplication.getString(R.string.id_open_ad_test)
        } else {
            myApplication.getString(R.string.id_open_ad)
        }
        
        val preloadConfig = PreloadConfiguration(AdRequestProvider.get(adUnitId))
        AppOpenAdPreloader.start(adUnitId, preloadConfig)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    /** Shows the ad if one isn't already showing.  */
    private fun showAdIfAvailable() {
        if (!myApplication.isAdsInitialized) return

        val adUnitId = if (BuildConfig.DEBUG) {
            myApplication.getString(R.string.id_open_ad_test)
        } else {
            myApplication.getString(R.string.id_open_ad)
        }

        if (!isShowingAd) {
            val appOpenAd = AppOpenAdPreloader.pollAd(adUnitId)
            if (appOpenAd != null) {
                appOpenAd.adEventCallback = object : AppOpenAdEventCallback {
                    override fun onAdDismissedFullScreenContent() {
                        isShowingAd = false
                    }
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
                currentActivity?.let { appOpenAd.show(it) }
            } else {
                fetchAd()
            }
        }
    }
}
