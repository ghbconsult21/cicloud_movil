package com.example.cicloud

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cicloud.network.SessionManager
import com.example.cicloud.pages.*
import kotlinx.coroutines.launch

enum class Screen(val title: String) {
    Login("Login"),
    Welcome("Bienvenido"),
    Reporteria("Reportería"),
    Asistencia("Mi Asistencia"),
    Marcacion("Registro Marcación"),
    Vacaciones("Solicitar Vacaciones")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Fecha estática
    val dateString = "20/05/2024"

    val isUserLoggedIn = SessionManager.isLoggedIn
    val showMenuElements = isUserLoggedIn && currentRoute != Screen.Login.name

    // Forzar cierre del drawer si estamos en el login
    LaunchedEffect(currentRoute) {
        if (currentRoute == Screen.Login.name) {
            drawerState.close()
        }
    }

    // Definición única del contenido de la App (Scaffold + NavHost)
    val appContent = @Composable {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1976D2),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    title = {
                        Column {
                            Text("Cicloud - control de asistencia", fontSize = 16.sp)
                            Text(dateString, fontSize = 12.sp)
                        }
                    },
                    navigationIcon = {
                        if (showMenuElements) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                            }
                        }
                    },
                    actions = {
                        if (showMenuElements) {
                            IconButton(onClick = {
                                GlobalMessageManager.showConfirm(
                                    title = "Confirmar",
                                    message = "¿Está seguro de cerrar sesión?",
                                    onConfirm = {
                                        SessionManager.isLoggedIn = false
                                        SessionManager.token = null
                                        SessionManager.institucionId = 0
                                        navController.navigate(Screen.Login.name) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                        GlobalMessageManager.dismiss()
                                    }
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp, 
                                    contentDescription = "Cerrar Sesión", 
                                    tint = Color(0xFFFF5252)
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                // FOOTER UNIFICADO
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    ) {
                        if (showMenuElements) {
                            // Fila de accesos rápidos
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { navController.navigate(Screen.Asistencia.name) }) {
                                    Text("Asistencia", fontSize = 12.sp)
                                }
                                TextButton(onClick = { navController.navigate(Screen.Reporteria.name) }) {
                                    Text("Reportes", fontSize = 12.sp)
                                }
                                TextButton(onClick = { navController.navigate(Screen.Vacaciones.name) }) {
                                    Text("Vacaciones", fontSize = 12.sp)
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                        
                        // Texto de soporte (Siempre visible)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Si requiere soporte comunicarse al 987654321",
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.name
                ) {
                    composable(Screen.Login.name) {
                        LoginScreen(onLoginSuccess = {
                            SessionManager.isLoggedIn = true
                            navController.navigate(Screen.Welcome.name) {
                                popUpTo(Screen.Login.name) { inclusive = true }
                            }
                        })
                    }
                    composable(Screen.Welcome.name) { WelcomeScreen() }
                    composable(Screen.Reporteria.name) { ReporteriaScreen() }
                    composable(Screen.Asistencia.name) { AsistenciaScreen() }
                    composable(Screen.Marcacion.name) { MarcacionScreen() }
                    composable(Screen.Vacaciones.name) { VacacionesScreen() }
                }
            }
        }
    }

    // Lógica para evitar bloqueos: Si no hay menú, no cargamos el Drawer en el árbol de UI
    if (showMenuElements) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menú Cicloud", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(Screen.Welcome.title) },
                        selected = currentRoute == Screen.Welcome.name,
                        onClick = { 
                            navController.navigate(Screen.Welcome.name)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(Screen.Marcacion.title) },
                        selected = currentRoute == Screen.Marcacion.name,
                        onClick = { 
                            navController.navigate(Screen.Marcacion.name)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(Screen.Asistencia.title) },
                        selected = currentRoute == Screen.Asistencia.name,
                        onClick = { 
                            navController.navigate(Screen.Asistencia.name)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(Screen.Reporteria.title) },
                        selected = currentRoute == Screen.Reporteria.name,
                        onClick = { 
                            navController.navigate(Screen.Reporteria.name)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(Screen.Vacaciones.title) },
                        selected = currentRoute == Screen.Vacaciones.name,
                        onClick = { 
                            navController.navigate(Screen.Vacaciones.name)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            },
            content = { appContent() }
        )
    } else {
        appContent()
    }
    
    // Alerta Global
    if (GlobalMessageManager.showAlert) {
        AlertDialog(
            onDismissRequest = { GlobalMessageManager.dismiss() },
            title = { Text(GlobalMessageManager.title) },
            text = { Text(GlobalMessageManager.message) },
            confirmButton = {
                Button(onClick = {
                    GlobalMessageManager.onConfirmAction?.invoke() ?: GlobalMessageManager.dismiss()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = if (GlobalMessageManager.isConfirmation) {
                {
                    TextButton(onClick = {
                        GlobalMessageManager.onCancelAction?.invoke() ?: GlobalMessageManager.dismiss()
                    }) {
                        Text("Cancelar")
                    }
                }
            } else null
        )
    }
}
