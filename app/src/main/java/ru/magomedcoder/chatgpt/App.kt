package ru.magomedcoder.chatgpt

import android.app.Application
import androidx.multidex.MultiDex
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.magomedcoder.chatgpt.di.allModules
import ru.magomedcoder.chatgpt.utils.persistence.Preference
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        initPreference()
        initMultiDex()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(allModules)
        }
    }

    private fun initPreference() {
        Preference.init(this, Constants.PREFERENCE_NAME)
    }

    private fun initMultiDex() {
        MultiDex.install(this)
    }

}