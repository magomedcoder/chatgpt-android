package ru.magomedcoder.chatgpt

import android.annotation.SuppressLint
import ru.magomedcoder.chatgpt.utils.persistence.Preference

object Constants {
    const val BASE_URL = "https://api.openai.com/"
    const val LOGGER_TAG = "chatgpt_log"
    const val DATABASE_NAME = "chatgpt_db"
    const val PREFERENCE_NAME = "chatgpt"
    const val API_KEY = "api_key"
    const val CHAT_MODEL = "chat_model"
}

@SuppressLint("StaticFieldLeak")
object GlobalConfig {
    var apiKey: String by Preference(Constants.API_KEY, "")
    var chatModel: String by Preference(Constants.CHAT_MODEL, "gpt-3.5-turbo")
}