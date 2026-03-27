# CI Cloud Móvil

Aplicación móvil para monitorear y gestionar pipelines de CI/CD en CI Cloud.

## Características

- 📊 **Panel de control** – Estadísticas en tiempo real de pipelines activos, fallidos y en cola
- ⚙️ **Gestión de Pipelines** – Lista completa con búsqueda y filtros por estado
- 🔔 **Notificaciones** – Alertas configurables para eventos de pipelines
- ⚙️ **Ajustes** – Configuración del servidor, token de acceso y preferencias

## Tecnologías

- [React Native](https://reactnative.dev/) con [Expo](https://expo.dev/)
- [React Navigation](https://reactnavigation.org/) – Navegación por pestañas

## Requisitos

- Node.js 18+
- Expo CLI (`npm install -g expo-cli`)
- Dispositivo o emulador Android/iOS (o Expo Go)

## Instalación

```bash
npm install
```

## Ejecución

```bash
# Iniciar el servidor de desarrollo
npm start

# Android
npm run android

# iOS
npm run ios

# Web
npm run web
```

## Estructura del proyecto

```
cicloud_movil/
├── App.js                  # Componente raíz con navegación
├── app.json                # Configuración de Expo
├── assets/                 # Imágenes e iconos
├── components/
│   └── PipelineCard.js     # Tarjeta de pipeline reutilizable
└── screens/
    ├── HomeScreen.js       # Panel de control principal
    ├── PipelinesScreen.js  # Lista de pipelines con filtros
    └── SettingsScreen.js   # Configuración de la aplicación
```
