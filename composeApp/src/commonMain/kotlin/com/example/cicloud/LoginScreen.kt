package com.example.cicloud

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import io.ktor.client.request.*
import com.example.cicloud.models.InstitucionDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedOptionText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    var instituciones by remember { mutableStateOf<List<InstitucionDto>>(emptyList()) }
    var labelText by remember { mutableStateOf("Cargando instituciones...") }

    LaunchedEffect(Unit) {
        try {
            val res = httpClient.get(Endpoints.getUrl(Endpoints.INSTITUCION_AUTOCOMPLETE))
                .process<List<InstitucionDto>>()
            
            if (res != null) {
                instituciones = res
                labelText = "Seleccione la institución"
            }
        } catch (e: Exception) {
            labelText = "Error al cargar datos"
        }
    }

    val filteredItems = remember(selectedOptionText, instituciones) {
        instituciones.filter { it.text.contains(selectedOptionText, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.LogoGhb),
            contentDescription = "Logo Cicloud",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Bienvenido a cicloud",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = selectedOptionText,
                onValueChange = {
                    selectedOptionText = it
                    expanded = true
                },
                label = { Text(labelText) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            if (filteredItems.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.text) },
                            onClick = {
                                selectedOptionText = item.text
                                expanded = false
                                SessionManager.institucionId = item.id
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        Button(
            onClick = { 
                // Simulación de login exitoso por ahora
                onLoginSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}
