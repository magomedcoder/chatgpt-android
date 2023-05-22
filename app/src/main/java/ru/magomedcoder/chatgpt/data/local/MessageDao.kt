package ru.magomedcoder.chatgpt.data.local

import androidx.room.Dao
import androidx.room.Query
import ru.magomedcoder.chatgpt.domain.model.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Query("SELECT * FROM messages WHERE dialogId = :dialogId")
    suspend fun fetchAll(dialogId: Int): List<Message>

    @Query("Delete FROM messages")
    suspend fun deleteAll()

}