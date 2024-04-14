package com.ring.ring.usecase.todo

import com.ring.ring.usecase.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TodoModel(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    @Serializable(with = InstantSerializer::class)
    val deadline: Instant,
)