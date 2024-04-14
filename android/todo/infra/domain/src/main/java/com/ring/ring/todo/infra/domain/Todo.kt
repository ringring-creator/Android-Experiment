package com.ring.ring.todo.infra.domain

import kotlinx.datetime.Instant

data class Todo(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Instant,
)