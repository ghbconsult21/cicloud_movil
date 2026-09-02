# Reglas de Arquitectura — cicloud_movil

## Estructura de Capas
- **`pages/` o `ui/`**: Pantallas `@Composable` que solo muestran estado y envían eventos al ViewModel.
- **`viewmodels/`**: Gestión de estado con `StateFlow` o `mutableStateOf`. **Nunca importar paquetes de UI de Compose en el ViewModel**.
- **`repository/`**: Capa de abstracción de datos. Conecta con Ktor o almacenamiento local.
- **`models/`**: Clases de datos anotadas con `@Serializable`.
- **`di/`**: Módulos Koin para proveer dependencias.

## Flujo de Datos Unidireccional
`UI (@Composable)` ➔ `ViewModel` ➔ `Repository` ➔ `Ktor Client` ➔ `API CicloudBE`
