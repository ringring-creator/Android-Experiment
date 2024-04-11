package com.ring.ring.infra.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class TodoEntity(
    @PrimaryKey val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Instant,
)