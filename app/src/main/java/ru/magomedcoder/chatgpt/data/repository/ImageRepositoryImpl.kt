package ru.magomedcoder.chatgpt.data.repository

import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.data.local.dao.ImageDao
import ru.magomedcoder.chatgpt.data.remote.ImageApi
import ru.magomedcoder.chatgpt.data.remote.ImageRequest
import ru.magomedcoder.chatgpt.data.remote.ImageResponse
import ru.magomedcoder.chatgpt.domain.model.Image
import ru.magomedcoder.chatgpt.domain.repository.ImageRepository
import ru.magomedcoder.chatgpt.utils.Failure
import ru.magomedcoder.chatgpt.utils.http.NetworkHandler

class ImageRepositoryImpl(
    private val _imageApi: ImageApi,
    private val _imageDao: ImageDao,
    private val _netWorkHandler: NetworkHandler
) : ImageRepository {

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

    suspend fun fetchImage(text: String): Result<ImageResponse> {
        return handleRemoteException {
            val imageRequest = ImageRequest(1, text, "512x512")
            val response =
                _imageApi.generations("Bearer ${Constants.GlobalConfig.apiKey}", imageRequest)
            response
        }
    }

    fun insertImage(image: Image): Result<Long> {
        return handleException {
            _imageDao.insert(image)
        }
    }

    fun queryAllImages(): Result<List<Image>> {
        return handleException {
            _imageDao.queryAll()
        }
    }
}