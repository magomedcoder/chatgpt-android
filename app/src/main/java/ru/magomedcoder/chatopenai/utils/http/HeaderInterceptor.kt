package ru.magomedcoder.chatopenai.utils.http

import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncodeUtils
import okhttp3.Interceptor
import okhttp3.Response
import ru.magomedcoder.chatopenai.BuildConfig

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authKey =
            "Bearer ${ConvertUtils.bytes2String(EncodeUtils.base64Decode(BuildConfig.OPEN_AI_KEY))}"
        val request = chain.request().newBuilder()
            .addHeader("Authorization", authKey)
            .build()
        return chain.proceed(request)
    }

}