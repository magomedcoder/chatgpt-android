package ru.magomedcoder.chatgpt.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(list: List<T>)

}