package ru.magomedcoder.chatopenai.data.local

import androidx.room.Dao
import androidx.room.Query
import ru.magomedcoder.chatopenai.domain.model.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Query("SELECT * FROM message")
    suspend fun fetchAll(): List<Message>

    @Query("Delete FROM message")
    suspend fun deleteAll()

}