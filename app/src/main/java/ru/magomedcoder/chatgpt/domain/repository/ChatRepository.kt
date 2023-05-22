package ru.magomedcoder.chatgpt.domain.repository

import ru.magomedcoder.chatgpt.data.remote.ChatResponse
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.domain.model.MessageDTO

interface ChatRepository {

    suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse>

    fun insertMessage(message: Message): Result<Long>

    suspend fun getAllMessage(id: Int): Result<List<Message>>

    suspend fun clear(): Result<Unit>

}