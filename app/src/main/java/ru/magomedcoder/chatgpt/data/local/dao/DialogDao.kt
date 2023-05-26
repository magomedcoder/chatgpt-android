package ru.magomedcoder.chatgpt.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.magomedcoder.chatgpt.domain.model.Dialog

@Dao
interface DialogDao : BaseDao<Dialog> {

    @Query("SELECT * FROM dialogs ORDER BY lastDialogTime DESC")
    fun queryAllDialog(): List<Dialog>

    @Query("SELECT * FROM dialogs ORDER BY lastDialogTime DESC LIMIT  1 ")
    fun queryLeastDialog(): Dialog?

    @Query("UPDATE dialogs SET lastDialogTime=:lastDialogTime WHERE id=:id")
    fun updateDialogTime(id: Int, lastDialogTime: Long)

    @Query("UPDATE dialogs SET title=:title WHERE id=:id")
    fun updateDialogTitle(id: Int, title: String)

}