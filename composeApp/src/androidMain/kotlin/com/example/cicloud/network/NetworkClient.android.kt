package com.example.cicloud.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import android.annotation.SuppressLint

actual fun HttpClientConfig<*>.configurePlatformEngine() {
    (this as? HttpClientConfig<OkHttpConfig>)?.engine {
        config {
            val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }
        }
    }
}
