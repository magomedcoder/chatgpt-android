package ru.magomedcoder.chatopenai.data.repository

import ru.magomedcoder.chatopenai.BuildConfig
import ru.magomedcoder.chatopenai.data.remote.ChatApi
import ru.magomedcoder.chatopenai.data.remote.ChatRequest
import ru.magomedcoder.chatopenai.data.remote.ChatResponse
import ru.magomedcoder.chatopenai.domain.model.MessageDTO
import ru.magomedcoder.chatopenai.domain.repository.ChatRepository
import ru.magomedcoder.chatopenai.utils.Failure
import ru.magomedcoder.chatopenai.utils.http.NetworkHandler

class ChatRepositoryImpl(
    private val _chatApi: ChatApi,
    private val _netWorkHandler: NetworkHandler
) : ChatRepository {

    private inline fun <T> handleRemoteException(onCall: () -> T): Result<T> {
        return if (_netWorkHandler.isNetworkAvailable()) {
            try {
                Result.success(onCall.invoke())
            } catch (e: Throwable) {
                e.printStackTrace()
                Result.failure(Failure.OtherError(e))
            }
        } else {
            Result.failure(Failure.NetworkError)
        }
    }

    override suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse> {
        return handleRemoteException {
            val chatRequest = ChatRequest(messageList, BuildConfig.CHAT_MODEL)
            val response = _chatApi.completions(chatRequest)
            response
        }
    }

}