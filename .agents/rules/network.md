# Reglas de Red — cicloud_movil (Ktor & CicloudBE)

## Consumo del Backend
- Endpoint de autenticación móvil: `POST /api/Auth/movil`.
- **Cabeceras obligatorias**:
  - `Institucion: <id>` (identificador del tenant para la base de datos).
  - `Authorization: Bearer <token>` (en endpoints protegidos).
- Manejo de excepciones con `try-catch` capturando errores de red y propagando resultados mediante `Result<T>`.
- Rutas centralizadas en `Endpoints.kt`.
