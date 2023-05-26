package ru.magomedcoder.chatgpt.ui.screen

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.magomedcoder.chatgpt.data.remote.ChatResponse
import ru.magomedcoder.chatgpt.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatgpt.domain.model.Dialog
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.domain.model.MessageDTO
import ru.magomedcoder.chatgpt.domain.model.Speech
import ru.magomedcoder.chatgpt.domain.model.VolumeState
import ru.magomedcoder.chatgpt.utils.Enums
import ru.magomedcoder.chatgpt.utils.Failure
import java.util.Locale

class ChatViewModel(private val _chatRepositoryImpl: ChatRepositoryImpl) : ViewModel() {

    private val _list = MutableLiveData<List<Message>>()
    var list: LiveData<List<Message>> = _list
    private val _currentDialog = MutableLiveData<Dialog>()
    val currentDialog: LiveData<Dialog> = _currentDialog
    private val _dialogList = MutableLiveData<List<Dialog>>()
    val dialogList: LiveData<List<Dialog>> = _dialogList
    private val _volumeState = MutableLiveData<VolumeState>()
    val volumeState: LiveData<VolumeState> = _volumeState
    private var textToSpeech: TextToSpeech? = null
    private val _speech = mutableStateOf(Speech())
    val speech: State<Speech> = _speech
    var isBottom = true
    var alreadyDeleteMessage: Message? = null

    init {
        queryLeastDialog()
    }

    private fun updateMessageList(dialogId: Int, onUpdate: (MutableList<Message>) -> Unit) {
        if (getCurrentDialogId() == dialogId) {
            _list.value = _list.value?.toMutableList()?.apply {
                onUpdate.invoke(this)
            }
        }
    }

    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            isBottom = true
            if (message.content.isNotEmpty()) {
                updateDialogTitle(message.dialogId, "Диалог №" + message.dialogId)
            }
            updateDialogTime(message.dialogId)
            _chatRepositoryImpl.insertMessage(message).onSuccess {
                val dialogId = message.dialogId
                if (message.role == Enums.USER.roleName) {
                    updateMessageList(dialogId) {
                        it.add(message)
                    }
                    updateMessageList(dialogId) {
                        it.add(Message(content = "", role = Enums.ASSISTANT.roleName))
                    }
                } else {
                    updateMessageList(dialogId) {
                        if (it.isNotEmpty()) {
                            val iterator = it.iterator()
                            while (iterator.hasNext()) {
                                val msg = iterator.next()
                                if (msg.role == Enums.ASSISTANT.roleName && msg.content == "") {
                                    iterator.remove()
                                }
                            }
                        }
                    }
                    updateMessageList(dialogId) {
                        it.add(message)
                    }
                }
            }.onFailure {}
        }
    }

    fun deleteMessage(message: Message) {
        viewModelScope.launch {
            _chatRepositoryImpl.deleteMessage(message).onSuccess {
                _list.value = _list.value?.toMutableList()?.apply {
                    this.remove(message)
                }
            }
        }
    }

    private fun deleteMultiMessage(messageList: List<Message>) {
        viewModelScope.launch {
            _chatRepositoryImpl.deleteMessage(messageList).onSuccess {
                _list.value = _list.value?.toMutableList()?.apply {
                    val iterator = this.iterator()
                    while (iterator.hasNext()) {
                        val checkMessage = iterator.next()
                        if (messageList.contains(checkMessage)) {
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    fun retryMessage(position: Int) {
        _list.value?.let {
            val retryMessage = it[position]
            deleteMultiMessage(it.subList(position, it.size))
            sendMessage(retryMessage.content)
        }
    }

    fun sendMessage(content: String) {
        if (lastDialogId == getCurrentDialogId()) {
            task?.cancel(null)
        }
        task = viewModelScope.launch {
            val dialogId = getCurrentDialogId()
            val message = Message(
                dialogId = dialogId,
                content = content,
                role = Enums.USER.roleName
            )
            lastDialogId = dialogId
            removeLastEmptyMessage(dialogId)
            insertMessage(message = message)
            _chatRepositoryImpl.fetchMessage(mutableListOf<MessageDTO>().apply {
                _list.value?.filter { it.role != Enums.SYSTEM.roleName }?.forEach {
                    add(it.toDTO())
                }
                add(message.toDTO())
            }).onSuccess {
                data(it, dialogId)
            }.onFailure {
                if ((it as Failure.OtherError).throwable !is CancellationException) {
                    removeLastEmptyMessage(dialogId)
                    updateMessageList(message.dialogId) { list ->
                        list.add(
                            Message(
                                dialogId = dialogId,
                                content = it.toString(),
                                role = Enums.SYSTEM.roleName
                            )
                        )
                    }
                }
            }
        }
    }

    private fun removeLastEmptyMessage(dialogId: Int) {
        if (!list.value.isNullOrEmpty()) {
            val lastMessage = list.value!![list.value!!.size - 1]
            if (lastMessage.role == Enums.ASSISTANT.roleName && lastMessage.content.isEmpty()) {
                updateMessageList(dialogId) {
                    it.remove(lastMessage)
                }
            }
        }
    }

    private fun data(response: ChatResponse, dialogId: Int) {
        if (response.error != null) {
            insertMessage(
                Message(
                    content = response.error.message,
                    role = Enums.SYSTEM.roleName,
                    dialogId = dialogId,
                )
            )
        } else {
            response.choices.forEach {
                insertMessage(
                    Message(
                        content = filterDrayMessage(it.message.content), role = it.message.role,
                        dialogId = dialogId,
                    )
                )
            }
        }
    }

    fun startNewDialog() {
        setDialogDetail(null)
    }

    fun switchDialog(dialog: Dialog?) {
        isBottom = true
        setDialogDetail(dialog)
    }

    private fun queryAllDialog() {
        viewModelScope.launch {
            _chatRepositoryImpl.queryAllDialog().onSuccess {
                _dialogList.value = it
            }
        }
    }

    private fun setDialogDetail(dialog: Dialog?) {
        viewModelScope.launch {
            if (dialog == null) {
                val newDialog = Dialog(0, "", System.currentTimeMillis())
                _chatRepositoryImpl.createDialog(newDialog).onSuccess {
                    _currentDialog.value = newDialog.copy(id = it.toInt())
                    _list.value = listOf()
                }
            } else {
                _currentDialog.value = dialog!!
                _chatRepositoryImpl.queryMessageBySID(dialog.id).onSuccess { messages ->
                    _list.value = messages
                }
            }
            queryAllDialog()
        }
    }

    private fun getCurrentDialogId(): Int {
        return _currentDialog.value!!.id
    }

    private suspend fun updateDialogTime(dialogId: Int) {
        viewModelScope.launch {
            val time = System.currentTimeMillis()
            _chatRepositoryImpl.updateDialogTime(id = dialogId, time).onSuccess {
                _currentDialog.value = _currentDialog.value!!.copy(lastDialogTime = time)
            }
        }
    }

    private suspend fun updateDialogTitle(dialogId: Int, title: String) {
        viewModelScope.launch {
            _chatRepositoryImpl.updateDialogTitle(id = dialogId, title).onSuccess {
                _currentDialog.value = _currentDialog.value!!.copy(title = title)
                queryAllDialog()
            }
        }
    }

    private fun queryLeastDialog() {
        viewModelScope.launch {
            _chatRepositoryImpl.queryLeastDialog().onSuccess {
                setDialogDetail(it)
            }.onFailure {}
        }
    }

    fun deleteCurrentDialog() {
        viewModelScope.launch {
            _chatRepositoryImpl.clear(_currentDialog.value!!).onSuccess {
                queryLeastDialog()
            }.onFailure {}
        }
    }

    var task: Job? = null
    var lastDialogId = -1

    private fun filterDrayMessage(message: String): String {
        var newMessage = message
        val nextLineSymbol = "\n"
        while (newMessage.startsWith(nextLineSymbol)) {
            val index = newMessage.indexOf("\n")
            newMessage = newMessage.substring(index + nextLineSymbol.length, newMessage.length)
        }
        return newMessage
    }

    fun setVolumeState(touchDown: Boolean = false, touchUp: Boolean = false) {
        _volumeState.value = VolumeState()
        _volumeState.value = VolumeState(touchDown, touchUp)
    }

    fun onTextFieldValueChange(text: String) {
        _speech.value = speech.value.copy(
            text = text
        )
    }

    fun textToSpeech(context: Context) {
        _speech.value = speech.value.copy(
            isButtonEnabled = false
        )
        textToSpeech = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.let { txtToSpeech ->
                    txtToSpeech.language = Locale("ru")
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(_speech.value.text, TextToSpeech.QUEUE_ADD, null, null)
                }
            }
        }
        _speech.value = speech.value.copy(
            isButtonEnabled = true
        )
    }

}