package ru.magomedcoder.chatgpt.domain.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class MessageDTO(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
)