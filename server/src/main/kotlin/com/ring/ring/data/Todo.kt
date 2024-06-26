package com.ring.ring.data

import kotlinx.datetime.Instant

data class Todo(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Instant,
    val userId: Long,
)