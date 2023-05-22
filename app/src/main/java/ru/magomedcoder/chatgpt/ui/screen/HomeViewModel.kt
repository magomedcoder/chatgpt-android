package ru.magomedcoder.chatgpt.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.magomedcoder.chatgpt.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatgpt.domain.model.Dialog

class HomeViewModel(private val _chatRepository: ChatRepositoryImpl) : ViewModel() {

    private val _list = MutableLiveData<List<Dialog>>()
    val list: LiveData<List<Dialog>> = _list

    init {
        queryAll()
    }

    private fun queryAll() {
        viewModelScope.launch {
            _chatRepository.queryAllDialog().onSuccess {
                _list.value = it
            }
        }
    }

}