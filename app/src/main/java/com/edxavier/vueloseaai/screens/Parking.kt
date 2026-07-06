package com.edxavier.vueloseaai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Parking() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Los pasajeros que deseen dejar su vehiculo estacionado en el Aeropuerto, mientras realizan su viaje, pueden solicitar el servicio al cajero de turno.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Costo por noche: US\$ 11.47\u00A0dolares.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tarifas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(12.dp))

        ParkingCard(
            icon = Icons.Default.DirectionsCar,
            label = "Vehiculos Livianos",
            subtitle = "Hasta 5 personas",
            firstHour = "$1.64",
            additionalHalf = "$0.82"
        )

        Spacer(modifier = Modifier.height(8.dp))

        ParkingCard(
            icon = Icons.Default.AirportShuttle,
            label = "Vehiculos Semipesados",
            subtitle = "Hasta 18 personas",
            firstHour = "$3.82",
            additionalHalf = "$1.91"
        )

        Spacer(modifier = Modifier.height(8.dp))

        ParkingCard(
            icon = Icons.Default.DirectionsBus,
            label = "Vehiculos Pesados",
            subtitle = "18 personas a mas",
            firstHour = "$7.65",
            additionalHalf = "$3.82"
        )

        Spacer(modifier = Modifier.height(8.dp))

        ParkingCard(
            icon = Icons.Default.TwoWheeler,
            label = "Motocicleta",
            subtitle = null,
            firstHour = "$0.56",
            additionalHalf = "$0.28"
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ParkingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    subtitle: String?,
    firstHour: String,
    additionalHalf: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "1ra hora:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = firstHour,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Extra:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = additionalHalf,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
