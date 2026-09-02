# Reglas de UI — cicloud_movil (Compose Multiplatform)

## Pautas de Creación de Pantallas y Componentes
- Nomenclatura `PascalCase` en toda función `@Composable`.
- Incluir `modifier: Modifier = Modifier` como primer parámetro opcional en Composables reutilizables.
- Utilizar `MaterialTheme.colorScheme` o `ColorConstants`. Prohibido quemar valores de color en hexadecimal.
- Usar recursos compartidos (`Res.string.*`, `Res.drawable.*`).
- Centralizar mensajes de éxito/error con `GlobalMessageManager`.
