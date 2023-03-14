package ru.magomedcoder.chatopenai.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.magomedcoder.chatopenai.BuildConfig
import ru.magomedcoder.chatopenai.data.remote.ChatApi
import ru.magomedcoder.chatopenai.data.repository.ChatRepositoryImpl
import ru.magomedcoder.chatopenai.ui.screen.ChatViewModel
import ru.magomedcoder.chatopenai.utils.http.HttpClient
import ru.magomedcoder.chatopenai.utils.http.NetworkHandler

val appModule = module {
    single {
        HttpClient()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client((get() as HttpClient).getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { NetworkHandler(get()) }
}

val dataModule = module {
    single { (get() as Retrofit).create(ChatApi::class.java) }
    single { ChatRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    single { ChatViewModel(get()) }
}

val allModules = appModule + dataModule + viewModelModule