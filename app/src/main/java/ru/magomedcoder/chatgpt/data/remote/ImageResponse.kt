package ru.magomedcoder.chatgpt.data.remote

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("data") val `data`: List<ImageData>,
    @SerializedName("error") val error: Error?
)

data class ImageData(
    val url: String
)