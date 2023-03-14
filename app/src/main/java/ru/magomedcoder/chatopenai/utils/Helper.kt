package ru.magomedcoder.chatopenai.utils

import ru.magomedcoder.chatopenai.BuildConfig

object Helper {

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            com.orhanobut.logger.Logger.d(msg)
        }
    }

}