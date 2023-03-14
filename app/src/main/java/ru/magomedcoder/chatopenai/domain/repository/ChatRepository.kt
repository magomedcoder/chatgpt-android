package ru.magomedcoder.chatopenai.domain.repository

import ru.magomedcoder.chatopenai.data.remote.ChatResponse
import ru.magomedcoder.chatopenai.domain.model.Message
import ru.magomedcoder.chatopenai.domain.model.MessageDTO

interface ChatRepository {

    suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse>

}