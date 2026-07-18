package com.edxavier.vueloseaai

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp

class BaseApp: Application() {
    private lateinit var appOpenManager:AdsOpenManager

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("2391D45C5FBB101C3B5A692B3E866DFB"))
                .build()
        )
        MobileAds.initialize(this)
        appOpenManager = AdsOpenManager(this)
    }
}