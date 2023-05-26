package ru.magomedcoder.chatgpt.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Expose @SerializedName("role") val role: String,
    @Expose @SerializedName("content") val content: String,
    val dialogId: Int = 0,
    val responseTime: Long = 1,
    val insertTime: Long = System.currentTimeMillis(),
    var status: Int = 1
) {

    fun toDTO(): MessageDTO {
        return MessageDTO(role = role, content = content)
    }
}