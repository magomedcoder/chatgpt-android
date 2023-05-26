package ru.magomedcoder.chatgpt.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.magomedcoder.chatgpt.domain.model.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Query("SELECT * FROM messages")
    suspend fun queryAll(): List<Message>

    @Transaction
    @Query("SELECT * FROM messages WHERE dialogId=:dialogId")
    fun selectMessageByDialogID(dialogId: Int): MutableList<Message>

    @Query("DELETE FROM messages ")
    suspend fun deleteAll()

    @Query("DELETE FROM messages WHERE dialogId=:dialogId")
    suspend fun deleteAll(dialogId: Int)

}