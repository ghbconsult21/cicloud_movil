# Guía de Estándares de Desarrollo e IA - Proyecto Cicloud

Este documento sirve como referencia para desarrolladores y asistentes de IA para asegurar la consistencia en el código, la arquitectura y el flujo de trabajo del proyecto Cicloud.

---

## 1. Estándares de Código (Kotlin & Compose)

### 1.1. Nomenclatura
*   **Clases y Objetos:** `PascalCase` (ej: `LoginRepository`, `ColorConstants`).
*   **Funciones `@Composable`:** `PascalCase` (ej: `WelcomeScreen`, `CustomButton`).
*   **Funciones Estándar:** `camelCase` (ej: `calculateTotal()`, `fetchUserData()`).
*   **Variables y Propiedades:** `camelCase` (ej: `isLoading`, `userName`).
*   **Constantes:** `PascalCase` o `UPPER_SNAKE_CASE` (preferible `PascalCase` dentro de `object ColorConstants`).

### 1.2. Jetpack Compose
*   **Uso de Temas:** Evitar colores "hardcoded". Usar siempre `MaterialTheme.colorScheme` o `ColorConstants`.
*   **Modificadores:** El primer parámetro opcional de un Composable debe ser `modifier: Modifier = Modifier`.
*   **Estado:** Usar `by remember { mutableStateOf(...) }` en UI local y `StateFlow` o `mutableStateOf` en ViewModels.
*   **Previews:** Cada pantalla o componente importante debe tener su función `@Preview`.

---

## 2. Arquitectura (MVVM + Repository)

Se debe seguir estrictamente la estructura definida en `ESTRUCTURA_PROYECTO.md`:

1.  **Model:** DTOs y clases de datos en `com.example.cicloud.models`.
2.  **Network:** Clientes Ktor y Endpoints en `com.example.cicloud.network`.
3.  **Repository:** Lógica de datos y abstracción en `com.example.cicloud.repository`.
4.  **ViewModel:** Lógica de negocio de pantalla en `com.example.cicloud.viewmodels`.
5.  **View (Screens):** UI en `com.example.cicloud.pages`.

### Reglas de Oro:
*   El **ViewModel** nunca debe importar librerías de UI (como `androidx.compose.*`) excepto para estados básicos.
*   El **Repository** es el único encargado de decidir si los datos vienen de la red o de la caché.
*   Usar **Inyección de Dependencias (Koin)** para proveer instancias de Repositorios y ViewModels.

---

## 3. Convenciones de Commits

Usamos **Conventional Commits** para mantener un historial limpio y legible:

`tipo(alcance): descripción corta`

*   **feat:** Una nueva funcionalidad (ej: `feat(auth): implementar login con biometría`).
*   **fix:** Corrección de un error (ej: `fix(ui): corregir padding en pantalla de inicio`).
*   **docs:** Cambios en la documentación (ej: `docs: actualizar guía de desarrollo`).
*   **style:** Cambios que no afectan la lógica (espacios, formateo, falta de punto y coma).
*   **refactor:** Cambio de código que no corrige un error ni añade funcionalidad.
*   **perf:** Mejora de rendimiento.
*   **chore:** Tareas de mantenimiento, actualización de dependencias, configuración de gradle.

---

## 4. Gestión de Recursos

*   **Strings y Recursos:** Utilizar el sistema de recursos de Compose Multiplatform (`Res.string.xxx`, `Res.drawable.xxx`).
*   **Colores:** Centralizados en `com.example.cicloud.ColorConstants`.
*   **Mensajes Globales:** Usar `GlobalMessageManager` para mostrar alertas, errores o confirmaciones desde cualquier parte de la app.

---

## 5. Flujo de Trabajo para la IA

Cuando pidas ayuda a la IA o cuando la IA genere código, asegúrate de:

1.  **Contexto:** Indicar en qué capa de la arquitectura se está trabajando.
2.  **Idiomas:** El código y los comentarios deben seguir el estándar del proyecto (predominantemente español para documentación y nombres descriptivos en inglés o español según consistencia).
3.  **Manejo de Errores:** Siempre usar bloques `try-catch` en llamadas de red y reportar errores a través del `GlobalMessageManager`.
4.  **Null Safety:** Aprovechar al máximo el sistema de tipos de Kotlin para evitar `NullPointerException`.

---

## 6. Configuración de Entorno

*   No subir `local.properties` al repositorio.
*   Usar `environment.dev.properties` para desarrollo y `environment.prod.properties` para producción. Estas configuraciones se inyectan en `BuildValues`.
