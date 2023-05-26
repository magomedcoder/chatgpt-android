package ru.magomedcoder.chatgpt.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApi {

    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun completions(
        @Header("Authorization") authKey: String,
        @Body requestBody: ChatRequest
    ): ChatResponse

}