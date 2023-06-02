package ru.magomedcoder.chatgpt.ui.screen

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.magomedcoder.chatgpt.data.remote.ImageResponse
import ru.magomedcoder.chatgpt.data.repository.ImageRepositoryImpl
import ru.magomedcoder.chatgpt.domain.model.Image
import ru.magomedcoder.chatgpt.utils.toast
import java.io.File

class ImageViewModel(private val _imageRepositoryImpl: ImageRepositoryImpl) : ViewModel() {

    private val _list = MutableLiveData<List<Image>>()
    var list: LiveData<List<Image>> = _list

    init {
        queryAllDialog()
    }

    fun send(content: String) {
        viewModelScope.launch {
            _imageRepositoryImpl.fetchImage(content).onSuccess {
                data(it, content)
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

    private fun data(response: ImageResponse, text: String) {
        if (response.error != null) {

        } else {
            response.data.forEach {
                insert(Image(url = it.url, text = text))
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

    @SuppressLint("Range")
    fun download(ctx: Context, url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        var msg: String? = ""
        var lastMsg = ""
        val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }
        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(status)
                if (msg != lastMsg) {
                    MainScope().launch {
                        ctx.toast(msg)
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()
    }

    private fun statusMessage(status: Int): String {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Скачивание не удалось, пожалуйста, попробуйте еще раз"
            DownloadManager.STATUS_PAUSED -> "Приостановлено"
            DownloadManager.STATUS_PENDING -> "В ожидании"
            DownloadManager.STATUS_RUNNING -> "Загрузка..."
            DownloadManager.STATUS_SUCCESSFUL -> "Изображение успешно скачано"
            else -> "Здесь нет ничего для загрузки"
        }
        return msg
    }

}