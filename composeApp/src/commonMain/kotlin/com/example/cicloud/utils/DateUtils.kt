package com.example.cicloud.utils

import kotlinx.datetime.LocalDateTime

object DateUtils {
    /**
     * Formatea una cadena de fecha ISO (u opcionalmente con tiempo T) a formato dd/MM/yyyy.
     * Ejemplo: "2026-06-28T13:00:00" -> "28/06/2026"
     */
    fun formatIsoDateToDisplay(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "N/A"
        
        return try {
            // Extraemos solo la parte de la fecha antes de la T
            val datePart = isoString.substringBefore("T")
            val components = datePart.split("-")
            
            if (components.size == 3) {
                val year = components[0]
                val month = components[1]
                val day = components[2]
                "$day/$month/$year"
            } else {
                isoString
            }
        } catch (e: Exception) {
            isoString
        }
    }

    /**
     * Extrae la hora en formato HH:mm de una cadena ISO.
     * Ejemplo: "2026-06-28T13:00:00" -> "13:00"
     */
    fun formatIsoTimeToDisplay(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "--:--"
        return try {
            isoString.substringAfter("T").take(5)
        } catch (e: Exception) {
            "--:--"
        }
    }
}
