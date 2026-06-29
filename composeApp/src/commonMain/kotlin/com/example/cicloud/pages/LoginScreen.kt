package com.example.cicloud.pages

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
import com.example.cicloud.Res
import com.example.cicloud.LogoGhb
import com.example.cicloud.network.SessionManager
import com.example.cicloud.viewmodels.LoginViewModel
import com.example.cicloud.ui.components.CicloudTextField
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedOptionText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    val filteredItems = remember(selectedOptionText, uiState.instituciones) {
        uiState.instituciones.filter { it.text.contains(selectedOptionText, ignoreCase = true) }
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
            CicloudTextField(
                value = selectedOptionText,
                onValueChange = {
                    selectedOptionText = it
                    expanded = it.isNotEmpty() // Solo expandir si hay texto
                    viewModel.onSearchQueryChanged(it)
                },
                label = uiState.labelText,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true).fillMaxWidth()
            )

            if (expanded && filteredItems.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    // IMPORTANTE: Para evitar que pierda el foco, nos aseguramos que el TextField
                    // sea el que maneje el foco y el menú sea solo un popup visual.
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

        CicloudTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = "Usuario",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            enabled = !uiState.isLoading
        )

        CicloudTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            enabled = !uiState.isLoading
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        Button(
            onClick = { 
                viewModel.login(usuario, password)
            },
            enabled = !uiState.isLoading && usuario.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Iniciando..." else "Iniciar sesión")
        }
    }
}
