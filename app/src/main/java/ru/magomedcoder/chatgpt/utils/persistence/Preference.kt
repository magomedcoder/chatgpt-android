package ru.magomedcoder.chatgpt.utils.persistence

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(val name: String, private val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        private lateinit var persistence: BasePersistence
        private var fileName = "default"

        fun init(context: Context, fileName: String) {
            persistence = try {
                MMKV.initialize(context, context.applicationInfo.dataDir + "/mmkv/")
                MMKVPersistence()
            } catch (e: UnsatisfiedLinkError) {
                e.printStackTrace()
                SpPersistence()
            } catch (e: Exception) {
                e.printStackTrace()
                SpPersistence()
            }
            Companion.context = context.applicationContext
            Companion.fileName = fileName
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default!!)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <A> findPreference(name: String, default: A): A {
        return persistence.findPreference(name, default)
    }

    private fun <A> putPreference(name: String, value: A) {
        return persistence.putPreference(name, value)
    }

}
