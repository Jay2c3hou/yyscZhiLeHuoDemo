package com.example.yyscdemo.model

import android.annotation.SuppressLint
import com.intuit.sdp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @author : yysc
 * @date : 2024/11/19 20:35.
 * @description :
 */
object Api {
    private const val DEFAULT_TIMEOUT: Long = 30

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder().baseUrl("https://test.shiqu.zhilehuo.com/")
            .addConverterFactory(GsonConverterFactory.create()).client(unsafeOkHttpClient).build()
        return@lazy retrofit.create(ApiService::class.java)
    }

    private val unsafeOkHttpClient: OkHttpClient
        get() = try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
//             Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val okBuilder = OkHttpClient.Builder()
            okBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            okBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            okBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            okBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            okBuilder.addInterceptor(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                ) else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            )
            okBuilder.hostnameVerifier { _, _ -> true }
            okBuilder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    @SuppressLint("CustomX509TrustManager")
    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })
}