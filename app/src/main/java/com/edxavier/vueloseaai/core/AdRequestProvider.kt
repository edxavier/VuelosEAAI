package com.edxavier.vueloseaai.core

import com.google.android.gms.ads.AdRequest

object AdRequestProvider {
    fun get(): AdRequest = AdRequest.Builder().build()
}
