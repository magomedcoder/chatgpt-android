package ru.magomedcoder.chatopenai.data.repository

import ru.magomedcoder.chatopenai.BuildConfig
import ru.magomedcoder.chatopenai.data.local.MessageDao
import ru.magomedcoder.chatopenai.data.remote.ChatApi
import ru.magomedcoder.chatopenai.data.remote.ChatRequest
import ru.magomedcoder.chatopenai.data.remote.ChatResponse
import ru.magomedcoder.chatopenai.domain.model.Message
import ru.magomedcoder.chatopenai.domain.model.MessageDTO
import ru.magomedcoder.chatopenai.domain.repository.ChatRepository
import ru.magomedcoder.chatopenai.utils.Failure
import ru.magomedcoder.chatopenai.utils.http.NetworkHandler

class ChatRepositoryImpl(
    private val _chatApi: ChatApi,
    private val _chatDao: MessageDao,
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

    private inline fun <T> handleException(onCall: () -> T): Result<T> {
        return try {
            Result.success(onCall.invoke())
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(Failure.OtherError(e))
        }

    }

    override fun insertMessage(message: Message): Result<Long> {
        return handleException {
            _chatDao.insert(message)
        }
    }

    override suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse> {
        return handleRemoteException {
            val chatRequest = ChatRequest(messageList, BuildConfig.CHAT_MODEL)
            val response = _chatApi.completions(chatRequest)
            response
        }
    }

    override suspend fun getAllMessage(): Result<List<Message>> {
        return handleException {
            _chatDao.fetchAll()
        }
    }


    override suspend fun clear(): Result<Unit> {
        return handleException {
            _chatDao.deleteAll()
        }
    }

}