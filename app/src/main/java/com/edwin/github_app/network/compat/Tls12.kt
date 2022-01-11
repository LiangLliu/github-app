package com.edwin.github_app.network.compat

import android.os.Build
import android.util.Log
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by benny on 2018/4/25.
 */
fun OkHttpClient.Builder.enableTls12OnPreLollipop(): OkHttpClient.Builder {
    if (Build.VERSION.SDK_INT in 16..21) {
        try {
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)

//            sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            val specs = listOf(cs, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)
            connectionSpecs(specs)
        } catch (exc: Exception) {
            Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
        }

    }

    return this
}

fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
    val naiveTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
    }

    val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
        val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
        init(null, trustAllCerts, SecureRandom())
    }.socketFactory

    sslSocketFactory(insecureSocketFactory, naiveTrustManager)
    hostnameVerifier(HostnameVerifier { _, _ -> true })
    return this
}

