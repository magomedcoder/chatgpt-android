package ru.magomedcoder.chatopenai.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.magomedcoder.chatopenai.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatopenai.domain.model.Message
import ru.magomedcoder.chatopenai.utils.Helper

class ChatViewModel(private val _chatRepository: ChatRepositoryImpl) : ViewModel() {

    private val _localList = MutableLiveData<List<Message>>()
    var localList: LiveData<List<Message>> = _localList

    private fun getAllMessage() {
        viewModelScope.launch {
            _chatRepository.fetchMessage().onSuccess {
                Helper.log(it.toString())
            }.onFailure { }
        }
    }

}