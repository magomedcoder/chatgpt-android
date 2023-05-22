package ru.magomedcoder.chatgpt.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialogs")
data class Dialog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val last: Long
)