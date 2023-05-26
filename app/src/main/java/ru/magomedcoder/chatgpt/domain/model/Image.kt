package ru.magomedcoder.chatgpt.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Expose @SerializedName("url") val url: String,
)
