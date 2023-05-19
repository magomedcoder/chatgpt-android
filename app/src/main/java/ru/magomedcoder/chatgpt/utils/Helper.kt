package ru.magomedcoder.chatgpt.utils

import ru.magomedcoder.chatgpt.BuildConfig

object Helper {

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            com.orhanobut.logger.Logger.d(msg)
        }
    }

}