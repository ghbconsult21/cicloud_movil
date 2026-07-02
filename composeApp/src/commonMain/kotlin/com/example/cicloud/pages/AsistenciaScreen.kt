package com.example.cicloud.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.utils.HardwareProvider
import com.example.cicloud.utils.LocationPermissionRequester
import com.example.cicloud.ColorConstants
import com.example.cicloud.models.MarcacionDto
import com.example.cicloud.utils.DateUtils
import com.example.cicloud.viewmodels.AsistenciaViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsistenciaScreen(
    viewModel: AsistenciaViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    var showPermissionRequester by remember { mutableStateOf(false) }
    var pendingMarcacion by remember { mutableStateOf<Pair<Int, MarcacionDto>?>(null) }

    if (showPermissionRequester) {
        LocationPermissionRequester(
            onPermissionGranted = {
                showPermissionRequester = false
                pendingMarcacion?.let { (proceso, marcacion) ->
                    viewModel.registrarFuncion(proceso, marcacion)
                }
                pendingMarcacion = null
            },
            onPermissionDenied = {
                showPermissionRequester = false
                pendingMarcacion = null
                GlobalMessageManager.show("Permiso Denegado", "Se requiere GPS para marcar asistencia", "error")
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Registro Asistencia", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.loadMarcaciones() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.error != null && uiState.marcaciones.isEmpty() -> {
                    EmptyOrErrorState(message = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
                uiState.marcaciones.isEmpty() && !uiState.isLoading -> {
                    EmptyOrErrorState(message = "No se encontraron marcaciones. Deslice para actualizar.")
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.marcaciones, key = { it.id }) { marcacion ->
                            MarcacionCard(
                                marcacion = marcacion,
                                onActionClick = { proceso ->
                                    if (HardwareProvider.hasLocationPermission()) {
                                        viewModel.registrarFuncion(proceso, marcacion)
                                    } else {
                                        pendingMarcacion = proceso to marcacion
                                        showPermissionRequester = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarcacionCard(
    marcacion: MarcacionDto,
    onActionClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val fechaDisplay = DateUtils.formatIsoDateToDisplay(marcacion.fechaInicio)
            Text(
                text = "$fechaDisplay - ${marcacion.horarioCodigo ?: "N/A"}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Horas Programadas
            val horaInicio = DateUtils.formatIsoTimeToDisplay(marcacion.fechaInicio)
            val horaFin = DateUtils.formatIsoTimeToDisplay(marcacion.fechaFin)

            Row(modifier = Modifier.fillMaxWidth()) {
                TimeInfoColumn(label = "Inicio Programado:", time = horaInicio, modifier = Modifier.weight(1f))
                TimeInfoColumn(label = "Fin Programado:", time = horaFin, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Horas Registradas
            Row(modifier = Modifier.fillMaxWidth()) {
                TimeInfoColumn(label = "Registro Entrada:", time = DateUtils.formatIsoTimeToDisplay(marcacion.horaRegistroEntrada), modifier = Modifier.weight(1f))
                TimeInfoColumn(label = "Registro Salida:", time = DateUtils.formatIsoTimeToDisplay(marcacion.horaRegistroSalida), modifier = Modifier.weight(1f))
            }

            // Botones y Etiquetas
            Spacer(modifier = Modifier.height(12.dp))
            ActionSection(
                registraIngreso = marcacion.registraIngreso,
                registraSalida = marcacion.registraSalida,
                onActionClick = onActionClick
            )

            marcacion.conceptoAsistencia?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Concepto: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun TimeInfoColumn(label: String, time: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(time, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ActionSection(
    registraIngreso: Boolean,
    registraSalida: Boolean,
    onActionClick: (Int) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (registraIngreso) {
            Button(
                onClick = { onActionClick(1) },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text("REG. ENTRADA", maxLines = 1)
            }
        }
        if (registraSalida) {
            Button(
                onClick = { onActionClick(2) },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = ColorConstants.Warning),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text("REG. SALIDA", maxLines = 1)
            }
        }
    }
}

@Composable
fun EmptyOrErrorState(message: String, color: Color = Color.Unspecified) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = color, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
    }
}
