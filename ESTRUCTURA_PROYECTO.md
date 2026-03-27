# Guía de Estructura y Arquitectura - Proyecto Cicloud

Este documento explica cómo está organizado este proyecto para que cualquier desarrollador, incluso si es principiante, pueda entender dónde encontrar cada cosa y cómo funciona la aplicación.

## 1. ¿Qué tipo de proyecto es este?
Este es un proyecto de **Kotlin Multiplatform (KMP)** con **Compose Multiplatform**. 
- **Kotlin Multiplatform:** Permite compartir el código de la lógica (como conectarse a internet o procesar datos) entre Android e iOS.
- **Compose Multiplatform:** Permite compartir también la interfaz de usuario (lo que ves en pantalla) entre ambas plataformas.

---

## 2. Estructura de Carpetas Principal

En la raíz del proyecto verás estas carpetas importantes:

*   **`composeApp/`**: Aquí vive casi todo el código de la aplicación. Es donde escribimos la lógica y las pantallas que se comparten.
*   **`iosApp/`**: Contiene el código específico para que la aplicación funcione en iPhones (usualmente solo para configurar el arranque).
*   **`gradle/`**: Archivos de configuración del sistema que construye la aplicación.

### Dentro de `composeApp/src/`:
Aquí es donde ocurre la magia, dividido por "Source Sets":
*   **`commonMain/`**: El código que funciona tanto en Android como en iOS. **Aquí pasarás el 90% de tu tiempo.**
*   **`androidMain/`**: Código que solo funciona en Android (ej. permisos específicos o integraciones de Google).
*   **`iosMain/`**: Código que solo funciona en iOS.

---

## 3. Organización del Código en `commonMain`

Dentro de `commonMain/kotlin/com/example/cicloud`, el código se organiza siguiendo el patrón **MVVM (Model-View-ViewModel)** y una capa de **Repositorios**:

### 📁 `models`
Son las "plantillas" de los datos. Si tenemos un Usuario, aquí definimos qué datos tiene (nombre, email, id).

### 📁 `network`
Aquí está la lógica para hablar con el servidor (API).
- `NetworkClient.kt`: Configura cómo nos conectamos a internet.
- `Endpoints.kt`: Lista de direcciones URL a las que llamamos.

### 📁 `repository`
Es el intermediario. El ViewModel no le pregunta directamente a internet, le pregunta al Repositorio. El Repositorio decide si los datos vienen de internet o de una base de datos local.
- Ejemplo: `AuthRepository.kt` gestiona todo lo relacionado con el inicio de sesión.

### 📁 `viewmodels`
Aquí vive la lógica de las pantallas. 
- Controlan el "estado" (ej. ¿está cargando?, ¿hay un error?).
- Procesan los clics de los botones llamando a los repositorios.
- Ejemplo: `LoginViewModel.kt`.

### 📁 `pages` / `Screens`
Aquí están las pantallas (la interfaz visual). Se escriben usando **Jetpack Compose**.
- Son funciones marcadas con `@Composable`.
- No contienen lógica compleja, solo muestran lo que el ViewModel les dice.

### 📁 `di` (Dependency Injection)
Usamos una herramienta llamada **Koin**. Sirve para "inyectar" dependencias. En lugar de crear un Repositorio manualmente cada vez, Koin se encarga de crearlo y entregarlo a quien lo necesite automáticamente.

---

## 4. Flujo de Datos Típico

Cuando un usuario interactúa con la app, los datos fluyen así:

1.  **Vista (Screen):** El usuario pulsa un botón. La Vista avisa al **ViewModel**.
2.  **ViewModel:** Recibe el aviso y le pide datos al **Repository**.
3.  **Repository:** Se comunica con la carpeta **Network** para hacer una petición al servidor.
4.  **Respuesta:** El servidor responde, el Repository procesa los datos (**Models**) y se los devuelve al ViewModel.
5.  **Actualización:** El ViewModel actualiza su "estado" y la Vista se redibuja automáticamente para mostrar los nuevos datos.

---

## 5. Archivos de Configuración de Entorno
Verás archivos como `environment.dev.properties` y `environment.prod.properties`. Se usan para cambiar automáticamente cosas como la URL del servidor dependiendo de si estamos probando (desarrollo) o si la app ya está publicada (producción).

---

## Consejos para Principiantes
- **Si quieres crear una pantalla nueva:** Crea un archivo en `pages`, su correspondiente `ViewModel` en `viewmodels`, y regístralos en el sistema de navegación.
- **Si necesitas un dato nuevo de la API:** Crea el modelo en `models`, añade el endpoint en `Endpoints.kt` y la función en el `Repository`.
- **No pongas lógica pesada en las Screens:** Las Screens solo deben mostrar datos y enviar eventos al ViewModel.
