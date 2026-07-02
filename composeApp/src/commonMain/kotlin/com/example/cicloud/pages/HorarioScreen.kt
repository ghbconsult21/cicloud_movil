package com.example.cicloud.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cicloud.ColorConstants
import com.example.cicloud.models.HorarioItemDto
import com.example.cicloud.viewmodels.HorarioViewModel
import kotlinx.datetime.*
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun HorarioScreen(
    viewModel: HorarioViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    
    var currentMonth by remember { mutableStateOf(today.month) }
    var currentYear by remember { mutableStateOf(today.year) }
    
    var selectedEventId by remember { mutableStateOf<Int?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Cargar horarios al iniciar o cuando cambie mes/año
    LaunchedEffect(currentMonth, currentYear) {
        viewModel.loadHorarios(currentYear, currentMonth)
    }

    val monthName = when(currentMonth) {
        Month.JANUARY -> "Enero"
        Month.FEBRUARY -> "Febrero"
        Month.MARCH -> "Marzo"
        Month.APRIL -> "Abril"
        Month.MAY -> "Mayo"
        Month.JUNE -> "Junio"
        Month.JULY -> "Julio"
        Month.AUGUST -> "Agosto"
        Month.SEPTEMBER -> "Septiembre"
        Month.OCTOBER -> "Octubre"
        Month.NOVEMBER -> "Noviembre"
        Month.DECEMBER -> "Diciembre"
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDatePicker = true }
            ) {
                Text(
                    text = "$monthName $currentYear",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 16.dp).size(20.dp),
                    strokeWidth = 2.dp,
                    color = ColorConstants.Primary
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            IconButton(onClick = {
                val previousMonth = if (currentMonth == Month.JANUARY) Month.DECEMBER else Month.entries[currentMonth.ordinal - 1]
                if (currentMonth == Month.JANUARY) currentYear--
                currentMonth = previousMonth
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Anterior", tint = MaterialTheme.colorScheme.onBackground)
            }
            IconButton(onClick = {
                val nextMonth = if (currentMonth == Month.DECEMBER) Month.JANUARY else Month.entries[currentMonth.ordinal + 1]
                if (currentMonth == Month.DECEMBER) currentYear++
                currentMonth = nextMonth
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Siguiente", tint = MaterialTheme.colorScheme.onBackground)
            }
        }

        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            listOf("dom", "lun", "mar", "mié", "jue", "vie", "sáb").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        val calendarDays = getCalendarGrid(currentMonth, currentYear)
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize().weight(1f),
            userScrollEnabled = false
        ) {
            items(calendarDays) { date ->
                val isCurrentMonth = date?.month == currentMonth
                val dateStr = date?.toString() // YYYY-MM-DD
                val dayHorarios = uiState.horarios.find { it.fecha == dateStr }
                
                DayCell(
                    date = date,
                    isCurrentMonth = isCurrentMonth,
                    isToday = date == today,
                    events = dayHorarios?.items ?: emptyList(),
                    onEventClick = { selectedEventId = it.id }
                )
            }
        }
    }

    // Modal de Detalle
    if (selectedEventId != null) {
        AlertDialog(
            onDismissRequest = { selectedEventId = null },
            title = { Text("Detalle de Horario", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Cargando información del ID: $selectedEventId...", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Aquí se mostrará: Tardanza, Evaluación, Lugar de trabajo, etc.", color = Color.Gray, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedEventId = null }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (showDatePicker) {
        MonthYearPickerDialog(
            initialMonth = currentMonth,
            initialYear = currentYear,
            onDismiss = { showDatePicker = false },
            onConfirm = { m, y -> currentMonth = m; currentYear = y; showDatePicker = false }
        )
    }
}

@Composable
fun DayCell(
    date: LocalDate?,
    isCurrentMonth: Boolean,
    isToday: Boolean,
    events: List<HorarioItemDto>,
    onEventClick: (HorarioItemDto) -> Unit
) {
    Box(
        modifier = Modifier.aspectRatio(0.6f).border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (date != null) {
                Surface(
                    color = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = date.day.toString(),
                            color = if (isToday) MaterialTheme.colorScheme.onPrimary else if (isCurrentMonth) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                events.forEach { event ->
                    EventChip(event) { onEventClick(event) }
                }
            }
        }
    }
}

@Composable
fun EventChip(event: HorarioItemDto, onClick: () -> Unit) {
    val bgColor = if (event.bg.isNotEmpty()) {
        try { Color(parseHexColor(event.bg)) } catch (e: Exception) { Color.Transparent }
    } else {
        Color.Transparent
    }
    
    val txColor = if (event.tx.isNotEmpty()) {
        try { Color(parseHexColor(event.tx)) } catch (e: Exception) { MaterialTheme.colorScheme.onSurface }
    } else {
        MaterialTheme.colorScheme.onSurface // Color de texto por defecto para visibilidad
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .then(
                if (bgColor != Color.Transparent) {
                    Modifier
                        .background(bgColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .border(0.5.dp, bgColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                } else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = event.cod,
            color = txColor,
            fontSize = 8.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

fun parseHexColor(hex: String): Long {
    val cleanHex = hex.removePrefix("#")
    return if (cleanHex.length == 6) {
        ("FF$cleanHex").toLong(16)
    } else {
        cleanHex.toLong(16)
    }
}

@Composable
fun MonthYearPickerDialog(initialMonth: Month, initialYear: Int, onDismiss: () -> Unit, onConfirm: (Month, Int) -> Unit) {
    var selectedMonth by remember { mutableStateOf(initialMonth) }
    var selectedYear by remember { mutableStateOf(initialYear) }
    val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { selectedYear-- }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
                Text(text = selectedYear.toString(), fontWeight = FontWeight.Bold)
                IconButton(onClick = { selectedYear++ }) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) }
            }
        },
        text = {
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(200.dp)) {
                items(Month.entries) { month ->
                    val isSelected = month == selectedMonth
                    TextButton(
                        onClick = { selectedMonth = month },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(months[month.ordinal], fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(selectedMonth, selectedYear) }) { Text("ACEPTAR") } }
    )
}

fun getCalendarGrid(month: Month, year: Int): List<LocalDate?> {
    val firstDay = LocalDate(year, month, 1)
    val firstDayOffset = (firstDay.dayOfWeek.ordinal + 1) % 7 
    val grid = mutableListOf<LocalDate?>()
    repeat(firstDayOffset) { grid.add(null) }
    for (i in 1..getDaysInMonth(month, year)) { grid.add(LocalDate(year, month, i)) }
    while (grid.size < 42) { grid.add(null) }
    return grid
}

fun getDaysInMonth(month: Month, year: Int): Int = when(month) {
    Month.FEBRUARY -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
    Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
    else -> 31
}
