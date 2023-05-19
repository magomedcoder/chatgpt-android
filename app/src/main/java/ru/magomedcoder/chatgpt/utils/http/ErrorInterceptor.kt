package ru.magomedcoder.chatgpt.utils.http

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response = chain.proceed(request)
        if (response.code == 401) {
            val json = response.body.string()
            response = Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(response.protocol)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }
        return response
    }

}