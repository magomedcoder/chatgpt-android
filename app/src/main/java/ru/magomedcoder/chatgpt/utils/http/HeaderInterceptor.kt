package ru.magomedcoder.chatgpt.utils.http

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authKey =
            "Bearer key?"
        val request = chain.request().newBuilder()
            .addHeader("Authorization", authKey)
            .build()
        return chain.proceed(request)
    }

}