package com.edxavier.vueloseaai

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp

class BaseApp: Application() {
    private lateinit var appOpenManager:AdsOpenManager

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        MobileAds.initialize(this)
        appOpenManager = AdsOpenManager(this)
    }
}