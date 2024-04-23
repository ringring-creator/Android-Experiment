package com.ring.ring.infra.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoentity")
    fun getTodoListStream(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todoentity")
    suspend fun getAll(): List<TodoEntity>

    @Upsert
    suspend fun upsertList(todo: List<TodoEntity>)

    @Query("DELETE FROM todoentity")
    suspend fun deleteAll()

    @Query("UPDATE todoentity SET done = :done WHERE id = :id")
    suspend fun updateDone(id: Long, done: Boolean)
}