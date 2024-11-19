package com.example.yyscdemo.api

import com.intuit.sdp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

/**
 * @author : yysc
 * @date : 2024/11/19 20:35.
 * @description :
 */
object Api {
    private const val DEFAULT_TIMEOUT: Long = 30

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder().baseUrl("https://test.shiqu.zhilehuo.com/englishgpt/")
            .addConverterFactory(GsonConverterFactory.create()).client(unsafeOkHttpClient).build()
        return@lazy retrofit.create(ApiService::class.java)
    }

    private val unsafeOkHttpClient: OkHttpClient
        get() = try {
            val okBuilder = OkHttpClient.Builder()
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

}