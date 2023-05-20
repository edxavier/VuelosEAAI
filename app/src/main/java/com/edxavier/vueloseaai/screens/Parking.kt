package com.edxavier.vueloseaai.screens

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Parking() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            text = "Parqueo",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium,
            text = "Los pasajeros que deseen dejar su vehículo estacionado en el Aeropuerto, mientras realizan su viaje, pueden solicitar el servicio al cajero de turno.\n" +
                    "El costo por noche transcurrida asciende a US\$ 8.54 dólares.",
        )
        Spacer(Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            text = "Tarifas",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "Vehiculos Livianos (Hasta 5 personas)",
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Primera hora o fraccion",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$1.13"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Cada media hora adicional",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$0.56"
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "Vehiculos Semipesado (Hasta 18 personas)",
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Primera hora o fraccion",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$2.84"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Cada media hora adicional",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$1.42"
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "Vehiculos Pesados (18 personas a mas)",
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Primera hora o fraccion",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$5.69"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Cada media hora adicional",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$2.84"
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "Motocicleta",
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Primera hora o fraccion",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$0.56"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "Cada media hora adicional",
            )
            Text(
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                text = "$0.28"
            )
        }

    }
}