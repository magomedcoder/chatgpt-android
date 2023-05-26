package ru.magomedcoder.chatgpt.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.magomedcoder.chatgpt.domain.model.Image

@Dao
interface ImageDao : BaseDao<Image> {

    @Query("SELECT * FROM images")
    fun queryAll(): MutableList<Image>

}