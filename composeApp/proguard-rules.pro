# Reglas de ProGuard para Cicloud
# Ayuda a reducir el tamaño del APK eliminando código no utilizado.

# Conservar clases de Compose y Koin si es necesario
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Conservar modelos serializables (importante para Ktor/Kotlinx Serialization)
-keepattributes *Annotation*, EnclosingMethod, Signature
-keepclassmembers class com.example.cicloud.models.** { *; }
-keep @kotlinx.serialization.Serializable class * { *; }
