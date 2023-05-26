package ru.magomedcoder.chatgpt.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ImageApi {

    @POST("v1/images/generations")
    @Headers("Content-Type: application/json")
    suspend fun generations(
        @Header("Authorization") authKey: String,
        @Body requestBody: ImageRequest
    ): ImageResponse

}