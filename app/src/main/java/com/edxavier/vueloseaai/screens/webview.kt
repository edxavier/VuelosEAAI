package com.edxavier.vueloseaai.screens

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage


@Composable
fun WebView() {
    val mUrl = "https://www.eaai.com.ni/fids/vuelos_fids.php"

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(mUrl)
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}