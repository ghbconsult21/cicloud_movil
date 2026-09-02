<!-- Copilot guidance for cicloud_movil (concise, actionable) -->

# cicloud_movil — Copilot Instructions

Short, repo-specific guidance for AI coding agents working on this Kotlin Multiplatform / Compose Multiplatform mobile app.

- **Project root:** `build.gradle.kts`, `settings.gradle.kts`, `composeApp/`
- **Key commands:** `.\gradlew.bat :composeApp:assembleDebug` (Windows), `./gradlew :composeApp:assembleDebug` (macOS/Linux).

**Important directories & layers**
- `composeApp/src/commonMain/kotlin/com/example/cicloud/` — 90% of shared code lives here.
- `models/` — Data classes with `@Serializable` for `kotlinx.serialization`.
- `network/` — Ktor client configuration (`NetworkClient.kt`) and `Endpoints.kt`.
- `repository/` — Data repositories. Handle `try-catch` around Ktor calls and return `Result<T>`.
- `viewmodels/` — Screen state logic using `StateFlow`. Never import `androidx.compose.*` UI packages in ViewModels.
- `pages/` (or `ui/`) — `@Composable` screens and reusable widgets.
- `di/` — Koin modules for dependency injection.

**Architecture & patterns (what to know)**
- Architecture: MVVM + Repository pattern with unidirectional data flow.
- Compose standards: `PascalCase` for `@Composable`, first optional parameter `modifier: Modifier = Modifier`.
- Theming: Use `MaterialTheme.colorScheme` and `ColorConstants`. Avoid hardcoding hex colors.
- Global alerts: Use `GlobalMessageManager.showSuccess(...)` and `GlobalMessageManager.showError(...)`.

**Backend Integration (CicloudBE)**
- Mobile auth endpoint: `POST /api/Auth/movil`.
- Required headers: `Institucion: <id>` (tenant DB resolution) and `Authorization: Bearer <token>`.
- Environments: `environment.dev.properties` and `environment.prod.properties`.
