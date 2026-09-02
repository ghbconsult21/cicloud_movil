# Reglas Generales — cicloud_movil (Kotlin & Compose Multiplatform)

## Contexto del Proyecto
`cicloud_movil` es la aplicación móvil de CICLOUD para Android e iOS basada en Kotlin Multiplatform (KMP) y Compose Multiplatform.

## Comandos Esenciales
- Compilar Android Debug (Windows): `.\gradlew.bat :composeApp:assembleDebug`
- Compilar Android Debug (macOS/Linux): `./gradlew :composeApp:assembleDebug`
- Limpiar build: `.\gradlew.bat clean`

## Principios Clave
1. **Compartir en `commonMain`**: La lógica y la interfaz residen en `composeApp/src/commonMain/kotlin`.
2. **Patrón MVVM + Repository**: Desacoplar vistas de datos mediante ViewModels y Repositorios.
3. **Inyección con Koin**: No instanciar dependencias manualmente; usar módulos Koin en `di/`.
4. **Mensajes Centralizados**: Emplear `GlobalMessageManager` para alertas e interacción con el usuario.
5. **Entornos**: Configuración en `environment.dev.properties` y `environment.prod.properties`.
