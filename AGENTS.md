# CICLOUD Móvil (cicloud_movil) — Guía para Asistentes de IA

Esta guía define los estándares, arquitectura y restricciones para cualquier asistente de IA que trabaje en el repositorio móvil `cicloud_movil`.

## 1. Identidad del Proyecto y Tecnologías
- **Plataformas**: Android & iOS
- **Tecnología**: Kotlin Multiplatform (KMP) + Compose Multiplatform
- **Lenguaje**: Kotlin 2.x
- **Patrón**: MVVM + Repository Pattern
- **Inyección de Dependencias**: Koin (`di/`)
- **Red / HTTP**: Ktor Client (`network/NetworkClient.kt`, `Endpoints.kt`)
- **Serialización**: `kotlinx.serialization` (`models/`)
- **Tema / Estilos**: Material 3 / `CicloudTheme` y `ColorConstants`
- **Mensajería Global**: `GlobalMessageManager`

## 2. Organización del Código (`composeApp/src/commonMain/kotlin/com/example/cicloud/`)
- `models/`: Clases de datos anotadas con `@Serializable`.
- `network/`: Configuración de cliente HTTP Ktor y catálogo de endpoints.
- `repository/`: Capa de intermediación y orquestación de datos.
- `viewmodels/`: Gestión de estado de pantalla (usando `StateFlow` o `mutableStateOf`). Sin dependencias de UI Compose.
- `pages/` (o `ui/`): Pantallas y widgets `@Composable`.
- `di/`: Módulos Koin para inyección de dependencias.

## 3. Reglas Innegociables
1. **Flujo de capas**: La vista `@Composable` solo interactúa con el `ViewModel`; el `ViewModel` solo interactúa con los `Repository`.
2. **Sin UI en ViewModels**: Nunca importar paquetes de UI de Compose (`androidx.compose.*`) dentro de los ViewModels.
3. **Consumo de Backend (`CicloudBE`)**:
   - Login móvil: `POST /api/Auth/movil`.
   - Incluir cabecera `Institucion: <id>` en todas las peticiones para la selección de base de datos multitenant.
   - Enviar `Authorization: Bearer <token>` en endpoints protegidos.
4. **Colores y Recursos**:
   - No hardcodear colores hexadecimales en los Composables; usar `MaterialTheme.colorScheme` o `ColorConstants`.
   - Utilizar recursos compartidos (`Res.string.*`, `Res.drawable.*`).
5. **Ofuscación en Release**:
   - Todos los modelos deben conservar la anotación `@Serializable` para evitar roturas por R8/ProGuard.

## 4. Comandos de Terminal
```shell
# Compilar Android Debug (Windows)
.\gradlew.bat :composeApp:assembleDebug

# Compilar Android Debug (macOS/Linux)
./gradlew :composeApp:assembleDebug
```

## 5. Reglas Modulares Adicionales
- Cursor Rules: `.cursor/rules/*.mdc`
- Agent Rules: `.agents/rules/*.md`
- Documentación de arquitectura: [ESTRUCTURA_PROYECTO.md](file:///d:/Proyectos/Cicloud-V4/cicloud_movil/ESTRUCTURA_PROYECTO.md)
- Guía de desarrollo: [GUIA_DESARROLLO_IA.md](file:///d:/Proyectos/Cicloud-V4/cicloud_movil/GUIA_DESARROLLO_IA.md)
