package ru.magomedcoder.chatgpt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.domain.model.Dialog
import ru.magomedcoder.chatgpt.domain.model.Message

@Database(entities = [Dialog::class, Message::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDialogDao(): DialogDao
    abstract fun getMessageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDBInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = createRoomBuilder(context).build()
                    }
                }
            }
            return INSTANCE!!
        }

        private fun createRoomBuilder(context: Context): Builder<AppDatabase> {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, Constants.DATABASE_NAME
            ).allowMainThreadQueries()
        }
    }
}