package ru.magomedcoder.chatgpt.data.repository

import ru.magomedcoder.chatgpt.GlobalConfig
import ru.magomedcoder.chatgpt.data.local.DialogDao
import ru.magomedcoder.chatgpt.data.local.MessageDao
import ru.magomedcoder.chatgpt.data.remote.ChatApi
import ru.magomedcoder.chatgpt.data.remote.ChatRequest
import ru.magomedcoder.chatgpt.data.remote.ChatResponse
import ru.magomedcoder.chatgpt.domain.model.Dialog
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.domain.model.MessageDTO
import ru.magomedcoder.chatgpt.domain.repository.ChatRepository
import ru.magomedcoder.chatgpt.utils.Failure
import ru.magomedcoder.chatgpt.utils.http.NetworkHandler

class ChatRepositoryImpl(
    private val _chatApi: ChatApi,
    private val _dialogDao: DialogDao,
    private val _messageDao: MessageDao,
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

    fun createDialog(dialog: Dialog): Result<Long> {
        return handleException {
            _dialogDao.insert(dialog)
        }
    }

    fun queryAllDialog(): Result<List<Dialog>> {
        return handleException {
            _dialogDao.queryAll()
        }
    }

    override fun insertMessage(message: Message): Result<Long> {
        return handleException {
            _messageDao.insert(message)
        }
    }

    override suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse> {
        return handleRemoteException {
            val chatRequest = ChatRequest(messageList, GlobalConfig.chatModel)
            val response = _chatApi.completions(chatRequest)
            response
        }
    }

    override suspend fun getAllMessage(id: Int): Result<List<Message>> {
        return handleException {
            _messageDao.fetchAll(id)
        }
    }

    override suspend fun clear(): Result<Unit> {
        return handleException {
            _messageDao.deleteAll()
        }
    }

}