package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.edxavier.vueloseaai.R
import com.edxavier.vueloseaai.data.FlightData

@Composable
fun Flight(
    data: FlightData,
    onDetailsClick: ((flight:String) -> Unit)? = null,
) {

    ElevatedCard(modifier = Modifier
        .clickable {
            onDetailsClick?.let { it(data.flight) }
        }
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
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
                modifier = Modifier.size(56.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.flight,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )
                Text(
                    text = data.origin,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = data.status,
                    color = data.statusColor(),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = data.time,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
                if(data.gate.isNotEmpty()){
                    Text(
                        text = data.gate, style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }

        }
    }
}