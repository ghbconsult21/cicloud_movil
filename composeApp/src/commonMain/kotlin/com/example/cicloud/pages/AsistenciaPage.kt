package com.example.cicloud.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cicloud.viewmodels.AsistenciaViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AsistenciaPage(
    viewModel: AsistenciaViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Registro Asistencia", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
        } else if (uiState.marcaciones.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron marcaciones.")
            }
        } else {
            LazyColumn {
                items(uiState.marcaciones) { marcacion ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val fechaDisplay = marcacion.fechaInicio?.split("T")?.firstOrNull() ?: "N/A"
                            Text(
                                text = "$fechaDisplay - ${marcacion.horarioCodigo ?: "N/A"}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Horas Programadas (Extraídas de fechaInicio y fechaFin)
                            val horaInicio = marcacion.fechaInicio?.split("T")?.getOrNull(1)?.substring(0, 5) ?: "--:--"
                            val horaFin = marcacion.fechaFin?.split("T")?.getOrNull(1)?.substring(0, 5) ?: "--:--"
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Inicio Programado:", style = MaterialTheme.typography.labelSmall)
                                    Text(horaInicio, fontWeight = FontWeight.SemiBold)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Fin Programado:", style = MaterialTheme.typography.labelSmall)
                                    Text(horaFin, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Horas Registradas
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("H. Registro Entrada:", style = MaterialTheme.typography.labelSmall)
                                    Text(marcacion.horaRegistroEntrada ?: "--:--")
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("H. Registro Salida:", style = MaterialTheme.typography.labelSmall)
                                    Text(marcacion.horaRegistroSalida ?: "--:--")
                                }
                            }

                            // Botones y Etiquetas según el estado del "proceso"
                            Spacer(modifier = Modifier.height(12.dp))
                            when (marcacion.proceso) {
                                1 -> Button(
                                    onClick = { viewModel.registrarFuncion(1, marcacion) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text("REGISTRAR ENTRADA")
                                }
                                2 -> Button(
                                    onClick = { viewModel.registrarFuncion(2, marcacion) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("REGISTRAR SALIDA")
                                }
                                3 -> Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.Red,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "PROCEDA A REGULARIZAR SU HORA DE INGRESO",
                                        color = Color.White,
                                        modifier = Modifier.padding(8.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            marcacion.conceptoAsistencia?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Concepto: $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
