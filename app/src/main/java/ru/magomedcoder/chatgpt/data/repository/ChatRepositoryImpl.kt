package ru.magomedcoder.chatgpt.data.repository

import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.data.local.dao.DialogDao
import ru.magomedcoder.chatgpt.data.local.dao.MessageDao
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
    private val _messageDao: MessageDao,
    private val _dialogDao: DialogDao,
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

    fun queryMessageBySID(dialogID: Int): Result<List<Message>> {
        return handleException {
            _messageDao.selectMessageByDialogID(dialogID)
        }
    }

    suspend fun fetchMessage(messageList: List<MessageDTO>): Result<ChatResponse> {
        return handleRemoteException {
            val chatRequest = ChatRequest(messageList, Constants.GlobalConfig.chatModel)
            val response =
                _chatApi.completions("Bearer ${Constants.GlobalConfig.apiKey}", chatRequest)
            response
        }
    }

    fun insertMessage(message: Message): Result<Long> {
        return handleException {
            _messageDao.insert(message)
        }
    }

    fun insertMessages(messages: List<Message>): Result<Unit> {
        return handleException {
            _messageDao.insert(messages)
        }
    }

    fun deleteMessage(message: Message): Result<Unit> {
        return handleException {
            _messageDao.delete(message)
        }
    }

    fun deleteMessage(message: List<Message>): Result<Unit> {
        return handleException {
            _messageDao.delete(message)
        }
    }

    suspend fun queryAllMessage(): Result<List<Message>> {
        return handleException {
            _messageDao.queryAll()
        }
    }

    fun createDialog(dialog: Dialog): Result<Long> {
        return handleException {
            _dialogDao.insert(dialog)
        }
    }

    fun queryAllDialog(): Result<List<Dialog>> {
        return handleException {
            _dialogDao.queryAllDialog()
        }
    }

    fun queryLeastDialog(): Result<Dialog?> {
        return handleException {
            _dialogDao.queryLeastDialog()
        }
    }

    fun updateDialogTime(id: Int, lastDialognTime: Long): Result<Unit> {
        return handleException {
            _dialogDao.updateDialogTime(id, lastDialognTime)
        }
    }

    fun updateDialogTitle(id: Int, title: String): Result<Unit> {
        return handleException {
            _dialogDao.updateDialogTitle(id, title)
        }
    }

    suspend fun clear(dialog: Dialog): Result<Unit> {
        return handleException {
            _messageDao.deleteAll(dialog.id)
            _dialogDao.delete(dialog)
        }
    }

}