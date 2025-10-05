package com.edxavier.vueloseaai.screens

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.edxavier.vueloseaai.core.ui.LoadingIndicator
import com.edxavier.vueloseaai.data.FlightsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@Composable
fun WebView(viewUrl: String, viewModel: FlightsViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if(isLoading) {
        LoadingIndicator()
    }

    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    isLoading = true
                    errorMessage = null
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isLoading = false
                }

            }
            settings.javaScriptEnabled = true
            loadUrl(viewUrl)
        }
    }, update = {
        it.loadUrl(viewUrl)
    })
}