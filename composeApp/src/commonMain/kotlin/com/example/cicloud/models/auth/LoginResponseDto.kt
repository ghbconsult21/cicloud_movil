package com.example.cicloud.models.auth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
object FlexibleBooleanSerializer : KSerializer<Boolean?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FlexibleBoolean", PrimitiveKind.BOOLEAN)

    override fun deserialize(decoder: Decoder): Boolean? {
        val jsonDecoder = decoder as? JsonDecoder ?: return try {
            decoder.decodeBoolean()
        } catch (_: Exception) {
            false
        }
        val element = jsonDecoder.decodeJsonElement()
        if (element is JsonNull) return false
        val primitive = element.jsonPrimitive
        val boolVal = primitive.booleanOrNull
        if (boolVal != null) return boolVal
        val intVal = primitive.intOrNull
        if (intVal != null) return intVal != 0
        val content = primitive.content
        return content.equals("true", ignoreCase = true) || content == "1"
    }

    override fun serialize(encoder: Encoder, value: Boolean?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeBoolean(value)
        }
    }
}

@Serializable
data class LoginResponseDto(
    val token: String,
    val userId: String,
    val personId: Int,
    val rolId: String,
    @Serializable(with = FlexibleBooleanSerializer::class)
    val changedPassword: Boolean? = false,
    @Serializable(with = FlexibleBooleanSerializer::class)
    val changePassword: Boolean? = null
) {
    val isPasswordChanged: Boolean
        get() = (changedPassword == true) || (changePassword == true)
}
