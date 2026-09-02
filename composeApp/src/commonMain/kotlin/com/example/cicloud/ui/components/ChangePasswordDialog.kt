package com.example.cicloud.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cicloud.ColorConstants
import com.example.cicloud.viewmodels.ChangePasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordDialog(
    modifier: Modifier = Modifier,
    isMandatory: Boolean = false,
    initialOldPassword: String = "",
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    var currentPassword by remember(initialOldPassword) { mutableStateOf(initialOldPassword) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.clearState()
    }

    val isNewPasswordValid = newPassword.length >= 6
    val isConfirmValid = confirmPassword.isNotEmpty() && confirmPassword == newPassword
    val isFormValid = currentPassword.isNotEmpty() && isNewPasswordValid && isConfirmValid && !uiState.isLoading

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            if (!isMandatory && !uiState.isLoading) {
                onDismiss()
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isMandatory) ColorConstants.Warning else ColorConstants.Primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (isMandatory) "Actualización Obligatoria" else "Cambiar Contraseña",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isMandatory) 
                        "Por motivos de seguridad, debe actualizar su contraseña antes de continuar."
                    else 
                        "Ingrese su contraseña actual y la nueva contraseña para actualizarla.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Contraseña Actual
                Column {
                    CicloudTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Contraseña Actual *",
                        enabled = !uiState.isLoading,
                        visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                Icon(
                                    imageVector = if (showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Mostrar contraseña"
                                )
                            }
                        }
                    )
                }

                // Nueva Contraseña
                Column {
                    CicloudTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "Nueva Contraseña *",
                        enabled = !uiState.isLoading,
                        visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                Icon(
                                    imageVector = if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Mostrar nueva contraseña"
                                )
                            }
                        }
                    )
                    if (newPassword.isNotEmpty() && !isNewPasswordValid) {
                        Text(
                            text = "Mínimo 6 caracteres",
                            color = ColorConstants.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // Confirmar Nueva Contraseña
                Column {
                    CicloudTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirmar Nueva Contraseña *",
                        enabled = !uiState.isLoading,
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Mostrar confirmación"
                                )
                            }
                        }
                    )
                    if (confirmPassword.isNotEmpty() && !isConfirmValid) {
                        Text(
                            text = "Las contraseñas no coinciden",
                            color = ColorConstants.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.cambiarPassword(
                        oldPassword = currentPassword,
                        newPassword = newPassword,
                        confirmPassword = confirmPassword,
                        onSuccess = onSuccess
                    )
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorConstants.Primary
                )
            ) {
                Text(if (uiState.isLoading) "Guardando..." else "Cambiar Contraseña")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !uiState.isLoading
            ) {
                Text(if (isMandatory) "Cancelar" else "Cerrar")
            }
        }
    )
}
