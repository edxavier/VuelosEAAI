package com.edxavier.vueloseaai.core

import com.google.android.libraries.ads.mobile.sdk.common.AdRequest

object AdRequestProvider {
    fun get(adUnitId: String): AdRequest = AdRequest.Builder(adUnitId).build()
}
