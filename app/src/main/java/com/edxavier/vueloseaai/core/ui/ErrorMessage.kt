package com.edxavier.vueloseaai.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.vueloseaai.R

@Composable
fun ErrorIndicator(
    title: String,
    icon: ImageVector,
    description: String
) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val filter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            Image(
                modifier = Modifier.size(92.dp),
                imageVector = icon,
                contentDescription = null, colorFilter = filter)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}