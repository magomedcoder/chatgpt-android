package ru.magomedcoder.chatgpt.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.data.local.AppDatabase
import ru.magomedcoder.chatgpt.data.remote.ChatApi
import ru.magomedcoder.chatgpt.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.utils.http.HttpClient
import ru.magomedcoder.chatgpt.utils.http.NetworkHandler

val appModule = module {
    single {
        HttpClient()
    }
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client((get() as HttpClient).getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { NetworkHandler(get()) }
}

val dataModule = module {
    single { (get() as Retrofit).create(ChatApi::class.java) }
    single { AppDatabase.getDBInstance(get()).messageDao() }
    single { ChatRepositoryImpl(get(), get(), get()) }
}

val viewModelModule = module {
    single { ChatViewModel(get()) }
}

val allModules = appModule + dataModule + viewModelModule