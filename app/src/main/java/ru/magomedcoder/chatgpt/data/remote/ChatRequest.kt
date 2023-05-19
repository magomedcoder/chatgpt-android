package ru.magomedcoder.chatgpt.data.remote

import com.google.gson.annotations.SerializedName
import ru.magomedcoder.chatgpt.domain.model.MessageDTO

data class ChatRequest(
    @SerializedName("messages") val messages: List<MessageDTO>,
    @SerializedName("model") val model: String,
)