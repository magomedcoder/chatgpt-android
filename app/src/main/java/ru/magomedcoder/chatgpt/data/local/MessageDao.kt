package ru.magomedcoder.chatgpt.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.magomedcoder.chatgpt.domain.model.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Transaction
    @Query("SELECT * FROM messages WHERE dialogId=:dialogId")
    fun selectMessageByDialogID(dialogId: Int): MutableList<Message>

    @Query("DELETE FROM messages WHERE dialogId=:dialogId")
    suspend fun deleteAll(dialogId: Int)

}