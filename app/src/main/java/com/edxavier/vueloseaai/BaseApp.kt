package com.edxavier.vueloseaai

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp

class BaseApp: Application() {
    lateinit var appOpenManager: AdsOpenManager
    var isAdsInitialized by mutableStateOf(false)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appOpenManager = AdsOpenManager(this)
    }
}