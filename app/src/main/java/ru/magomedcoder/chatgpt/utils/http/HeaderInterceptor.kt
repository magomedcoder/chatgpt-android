package ru.magomedcoder.chatgpt.utils.http

import okhttp3.Interceptor
import okhttp3.Response
import ru.magomedcoder.chatgpt.GlobalConfig

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer  ${GlobalConfig.apiKey}")
            .build()
        return chain.proceed(request)
    }

}