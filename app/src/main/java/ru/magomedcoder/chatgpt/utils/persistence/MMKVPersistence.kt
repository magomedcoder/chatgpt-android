package ru.magomedcoder.chatgpt.utils.persistence

import com.tencent.mmkv.MMKV

class MMKVPersistence : BasePersistence {

    val mmkv: MMKV? by lazy { MMKV.defaultMMKV() }

    override fun <A> findPreference(name: String, default: A): A = with(mmkv!!) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> getString(name, serialize(default))?.let(this@MMKVPersistence::deSerialization)
        }!!
        res as A
    }

    override fun <A> putPreference(name: String, value: A) = with(mmkv!!) {
        var a = when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.commit()
    }

    override fun clearPreference() {
        mmkv?.edit()?.clear()?.commit()
    }

    override fun clearPreference(key: String) {
        mmkv?.edit()?.remove(key)?.commit()
    }
}