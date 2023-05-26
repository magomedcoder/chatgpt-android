package ru.magomedcoder.chatgpt.data.remote

import com.google.gson.annotations.SerializedName

data class ImageRequest(
    @SerializedName("n") val n: Int,
    @SerializedName("prompt") val prompt: String,
    @SerializedName("size") val size: String
)