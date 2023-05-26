package ru.magomedcoder.chatgpt.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.data.local.AppDatabase
import ru.magomedcoder.chatgpt.data.remote.ChatApi
import ru.magomedcoder.chatgpt.data.remote.ImageApi
import ru.magomedcoder.chatgpt.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatgpt.data.repository.ImageRepositoryImpl
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.ui.screen.ImageViewModel
import ru.magomedcoder.chatgpt.utils.http.HttpClient
import ru.magomedcoder.chatgpt.utils.http.NetworkHandler

val appModule = module {
    single {
        HttpClient()
    }
    single {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client((get() as HttpClient).getClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    single { NetworkHandler(get()) }

}
val dataModule = module {
    single { (get() as Retrofit).create(ChatApi::class.java) }
    single { (get() as Retrofit).create(ImageApi::class.java) }
    single { AppDatabase.getDBInstance(get()).getMessageDao() }
    single { AppDatabase.getDBInstance(get()).getDialogDao() }
    single { AppDatabase.getDBInstance(get()).getImageDao() }
    single { ChatRepositoryImpl(get(), get(), get(), get()) }
    single { ImageRepositoryImpl(get(), get(), get()) }
}
val viewModelModule = module {
    single { ChatViewModel(get()) }
    single { ImageViewModel(get()) }
}

val allModules = appModule + dataModule + viewModelModule