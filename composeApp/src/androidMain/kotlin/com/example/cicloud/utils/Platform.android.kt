package com.example.cicloud.utils

import android.os.Build
import android.content.Intent
import android.net.Uri

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun exitApp() {
    kotlin.system.exitProcess(0)
}

actual fun openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        AndroidHardwareManager.context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback or log
    }
}
