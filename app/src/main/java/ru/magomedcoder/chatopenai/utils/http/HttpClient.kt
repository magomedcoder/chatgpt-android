package ru.magomedcoder.chatopenai.utils.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class HttpClient {

    private val timeout = 30L

    private val mClient: OkHttpClient by lazy {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message: String -> Log.d("http", "HTTP-журнал: $message") }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    fun getClient(): OkHttpClient {
        return mClient
    }

}