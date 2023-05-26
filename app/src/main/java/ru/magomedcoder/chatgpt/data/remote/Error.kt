package ru.magomedcoder.chatgpt.data.remote

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("param")
    val `param`: Any,
    @SerializedName("type")
    val type: String
)