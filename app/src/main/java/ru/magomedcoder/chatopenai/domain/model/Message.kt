package ru.magomedcoder.chatopenai.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.magomedcoder.chatopenai.utils.enums.MessageStatus

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Expose @SerializedName("role") val role: String,
    @Expose @SerializedName("content") val content: String,
    val responseTime: Long = 1,
    val insertTime: Long = System.currentTimeMillis(),
    var status: Int = MessageStatus.UNFINISHED.status
) {
    fun toDTO(): MessageDTO {
        return MessageDTO(role = role, content = content)
    }
}