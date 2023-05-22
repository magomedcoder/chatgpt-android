package ru.magomedcoder.chatgpt.data.local

import androidx.room.Dao
import androidx.room.Query
import ru.magomedcoder.chatgpt.domain.model.Dialog

@Dao
interface DialogDao : BaseDao<Dialog> {

    @Query("SELECT * FROM dialogs ORDER BY last DESC")
    fun queryAll(): List<Dialog>

}