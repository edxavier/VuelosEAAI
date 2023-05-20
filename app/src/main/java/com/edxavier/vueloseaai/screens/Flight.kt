package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.data.FlightData

@Composable
fun Flight(data: FlightData) {
    
    Card(modifier = Modifier
        .clickable {}
        .fillMaxWidth()
        .padding(horizontal = 8.dp,),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            SubcomposeAsyncImage(
                model = data.logo, contentDescription = null,
                loading = {
                          Box(modifier = Modifier.fillMaxSize()) {
                              CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                          }
                },
                error = {
                    Image(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.image_placeholder
                        ),
                        contentDescription = null, )
                },
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.size(64.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.flight, style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = data.origin, style = MaterialTheme.typography.titleSmall
                )
            }
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = data.status, style = MaterialTheme.typography.bodyLarge,
                    color = data.statusColor(), textAlign = TextAlign.End,
                    fontWeight = FontWeight(600)
                )
                Text(
                    text = data.time, style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
                if(data.gate.isNotEmpty()){
                    Text(
                        text = data.gate, style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End
                    )
                }
            }

        }
    }
}