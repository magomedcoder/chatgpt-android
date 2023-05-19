package ru.magomedcoder.chatgpt

import android.app.Application
import androidx.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.magomedcoder.chatgpt.di.allModules
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        initLogger()
        initMultiDex()
        initKoin()
    }

    private fun initLogger() {
        val logcatStrategy: FormatStrategy =
            PrettyFormatStrategy
                .newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .methodOffset(1)
                .tag(Constants.LOGGER_TAG).build()
        Logger.addLogAdapter(AndroidLogAdapter(logcatStrategy))
    }

    private fun initKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(allModules)
        }
    }

    private fun initMultiDex() {
        MultiDex.install(this)
    }

}