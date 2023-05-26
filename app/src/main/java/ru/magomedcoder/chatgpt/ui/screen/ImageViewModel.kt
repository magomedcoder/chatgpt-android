package ru.magomedcoder.chatgpt.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.magomedcoder.chatgpt.data.remote.ImageResponse
import ru.magomedcoder.chatgpt.data.repository.ImageRepositoryImpl
import ru.magomedcoder.chatgpt.domain.model.Image

class ImageViewModel(private val _imageRepositoryImpl: ImageRepositoryImpl) : ViewModel() {

    private val _list = MutableLiveData<List<Image>>()
    var list: LiveData<List<Image>> = _list

    init {
        queryAllDialog()
    }

    fun send(content: String) {
        viewModelScope.launch {
            _imageRepositoryImpl.fetchImage(content).onSuccess {
                data(it)
            }.onFailure {

            }
        }
    }

    private fun updateImageList(onUpdate: (MutableList<Image>) -> Unit) {
        _list.value = _list.value?.toMutableList()?.apply {
            onUpdate.invoke(this)
        }
    }

    private fun insert(image: Image) {
        viewModelScope.launch {
            _imageRepositoryImpl.insertImage(image).onSuccess {
                updateImageList() {
                    it.add(image)
                }
            }.onFailure {}
        }
    }

    private fun data(response: ImageResponse) {
        if (response.error != null) {

        } else {
            response.data.forEach {
                insert(Image(url = it.url))
            }
        }
    }

    private fun queryAllDialog() {
        viewModelScope.launch {
            _imageRepositoryImpl.queryAllImages().onSuccess {
                _list.value = it
            }
        }
    }
}