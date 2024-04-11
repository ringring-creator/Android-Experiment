package com.ring.ring.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoentity")
    suspend fun getAll(): List<TodoEntity>

    @Upsert
    suspend fun upsertList(todo: List<TodoEntity>)

    @Query("DELETE FROM todoentity")
    suspend fun deleteAll()
}