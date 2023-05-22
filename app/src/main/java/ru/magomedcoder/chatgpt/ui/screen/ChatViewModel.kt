package ru.magomedcoder.chatgpt.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.magomedcoder.chatgpt.data.remote.ChatResponse
import ru.magomedcoder.chatgpt.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.domain.model.MessageDTO
import ru.magomedcoder.chatgpt.utils.Helper
import ru.magomedcoder.chatgpt.utils.enums.Role

class ChatViewModel(private val _chatRepository: ChatRepositoryImpl) : ViewModel() {

    private val _list = MutableLiveData<List<Message>>()
    var list: LiveData<List<Message>> = _list

    init {
        getAllMessage()
    }
    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            _chatRepository.insertMessage(message).onSuccess {
                if (message.role == Role.USER.roleName) {
                    updateList {
                        it.add(message)
                    }
                    updateList {
                        it.add(Message(content = "", role = Role.ASSISTANT.roleName))
                    }
                } else {
                    updateList {
                        it.removeAt(it.size - 1)
                    }
                    updateList {
                        it.add(message)
                    }
                }
                Helper.log("Сообщение $message")
            }.onFailure { }
        }
    }

    fun sendContent(content: String) {
        val message = Message(content = content, role = Role.USER.roleName)
        viewModelScope.launch {
            _chatRepository.fetchMessage(mutableListOf<MessageDTO>().apply {
                _localList.value?.filter { it.role != Role.SYSTEAM.roleName }?.forEach {
                    add(it.toDTO())
                }
                insertMessage(message = message)
                add(message.toDTO())
            }).onSuccess {
                dataProcess(it)
            }.onFailure {
                insertMessage(Message(content = it.toString(), role = Role.SYSTEAM.roleName))
            }
        }
    }

    fun getAllMessage() {
        viewModelScope.launch {
            _chatRepository.getAllMessage(getDialogId()).onSuccess {
                _list.value = it
                Helper.log(it.toString())
            }.onFailure { }
        }
    }

    private fun dataProcess(gtpResponse: ChatResponse) {
        if (gtpResponse.error != null) {
            insertMessage(
                Message(
                    content = gtpResponse.error.message,
                    role = Role.SYSTEAM.roleName
                )
            )
        } else {
            gtpResponse.choices.forEach {
                insertMessage(
                    Message(
                        content = filterDrayMessage(it.message.content),
                        role = it.message.role
                    )
                )
            }
        }
    }

    private fun updateList(onUpdate: (MutableList<Message>) -> Unit) {
        _list.value = _list.value?.toMutableList()?.apply {
            onUpdate.invoke(this)
        }
    }

    private fun filterDrayMessage(message: String): String {
        var newMessage = message
        val nextLineSymbol = "\n"
        while (newMessage.startsWith(nextLineSymbol)) {
            val index = newMessage.indexOf("\n")
            newMessage = newMessage.substring(index + nextLineSymbol.length, newMessage.length)
        }
        return newMessage
    }

    fun clear() {
        viewModelScope.launch {
            _chatRepository.clear().onSuccess {
                getAllMessage()
            }.onFailure {}
        }
    }

}