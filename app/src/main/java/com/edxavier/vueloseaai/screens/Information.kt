package com.edxavier.vueloseaai.screens

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.edxavier.vueloseaai.BuildConfig
import com.edxavier.vueloseaai.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

@Composable
fun Information() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = BuildConfig.VERSION_NAME,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val myContext = LocalContext.current
            ExtendedFloatingActionButton(onClick = {
                Firebase.analytics.logEvent("share_app", null)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, myContext.getString(R.string.app_name))
                    putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${myContext.packageName}")
                }
                startActivity(myContext,createChooser(intent, "Compartir app..."),null)
            }) {
                Text(text = "Compartir")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Share, contentDescription = "")
            }
            ExtendedFloatingActionButton(onClick = {
                Firebase.analytics.logEvent("rate_app", null)
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${myContext.packageName}"))
                    startActivity(myContext,createChooser(intent, "Calificar app..."),null)
                }catch (_:Exception){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${myContext.packageName}"))
                    startActivity(myContext,createChooser(intent, "Calificar app..."),null)
                }
            }) {
                Text(text = "Calificar")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Star, contentDescription = "")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Parking()
    }
}